package hypervolt

import com.twitter.util.{Await, Future, Promise}
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.core.client.config.{
  ClientOverrideConfiguration,
  SdkAdvancedAsyncClientOption
}
import software.amazon.awssdk.core.retry.RetryPolicy
import software.amazon.awssdk.core.retry.backoff.FullJitterBackoffStrategy
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient
import software.amazon.awssdk.services.kinesis.model.{
  PutRecordsRequest,
  PutRecordsRequestEntry,
  PutRecordsResponse
}

import java.nio.ByteBuffer
import scala.jdk.CollectionConverters._
import java.time.{Instant, Duration => JDuration}
import java.util
import java.util.concurrent.{CompletableFuture, Executor}

// hypervolt.TestKinesisPut.apply()
object TestKinesisPut {
  def apply(): Unit = {
    val kinesisClient: KinesisAsyncClient = makeClient()

    val r = Halogen(
      schema = 0,
      // Not used anyway, see CLOUD-671
      commit = 0,
      itemId = 40,
      device = 2377901702763249664L,
      timestamp = Instant.now().toEpochMilli,
      isRefresh = false,
      telemetry = HalogenShort(
        name = "pilot_status",
        data = 65
      )
    ).toKinesisPutEntry

    System.err.println("Calling putRecord")
    Await.result(putRecord("staging-halogen-telemetry", r, kinesisClient))
    System.err.println("Got here!")
  }

  def putRecord(
      streamName: String,
      record: PutRecordsRequestEntry,
      client: KinesisAsyncClient
  ): Future[PutRecordsResponse] =
    putRecords(streamName, List(record).asJava, client)

  def putRecords(
      streamName: String,
      records: util.Collection[PutRecordsRequestEntry],
      client: KinesisAsyncClient
  ): Future[PutRecordsResponse] =
    client
      .putRecords(
        PutRecordsRequest
          .builder()
          .streamName(streamName)
          .records(records)
          .build()
      )
      .asTwitter

  def makeClient(
      region: Region = Region.of("eu-west-1"),
      includeNettyClient: Boolean = false,
      futureCompletionExecutor: Boolean = false,
      retryConfig: Option[RetryConfig] = None
  ): KinesisAsyncClient = {
    val base =
      KinesisAsyncClient
        .builder()
        .region(region)
        .credentialsProvider(DefaultCredentialsProvider.create())

    val withNetty =
      if (includeNettyClient)
        base.httpClient(
          NettyNioAsyncHttpClient
            .builder()
            .connectionAcquisitionTimeout(JDuration.ofSeconds(20))
            .maxPendingConnectionAcquires(20480)
            .build()
        )
      else
        base

    val withRetry =
      retryConfig
        .map(makeOverrideConfig)
        .map(withNetty.overrideConfiguration)
        .getOrElse(withNetty)

    (if (futureCompletionExecutor) {
       val executor: Executor = r => r.run()
       withRetry
         .asyncConfiguration(
           _.advancedOption(
             SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR,
             executor
           )
         )
     } else {
       withRetry
     })
      .build()
  }

  def makeOverrideConfig(
      retryConfig: RetryConfig
  ): ClientOverrideConfiguration =
    ClientOverrideConfiguration
      .builder()
      .retryPolicy(
        RetryPolicy
          .builder()
          .numRetries(retryConfig.numRetries)
          .backoffStrategy(
            FullJitterBackoffStrategy
              .builder()
              .maxBackoffTime(
                JDuration.ofSeconds(retryConfig.maxBackoffSeconds)
              )
              .baseDelay(JDuration.ofMillis(retryConfig.baseDelayMillis))
              .build()
          )
          .build()
      )
      .build()

  implicit class ToFutureCF[T](val x: CompletableFuture[T]) extends AnyVal {
    @annotation.nowarn("cat=w-flag-value-discard")
    def asTwitter: Future[T] = {
      val p = Promise[T]()
      x.whenComplete { (v, t) =>
        if (v != null) {
          p.setValue(v)
        } else {
          p.setException(t)
        }
      }
      p
    }
  }

  def reqFromBytes(data: Array[Byte], pk: String): PutRecordsRequestEntry =
    reqFromByteBuf(ByteBuffer.wrap(data), pk)

  def reqFromByteBuf(data: ByteBuffer, pk: String): PutRecordsRequestEntry =
    PutRecordsRequestEntry
      .builder()
      .data(SdkBytes.fromByteBuffer(data))
      .partitionKey(pk)
      .build()
}

case class RetryConfig(
    numRetries: Int = 3,
    maxBackoffSeconds: Long = 10,
    baseDelayMillis: Long = 100
)
