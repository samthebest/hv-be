package hypervolt

import com.twitter.finagle.util.DefaultTimer.Implicit
import com.twitter.util.Timer
import io.netty.channel.embedded.EmbeddedChannel
import io.netty.channel.{ChannelHandlerContext, SimpleChannelInboundHandler}

// import hypervolt.TestChannel
object TestChannel {
  def apply(): Unit = {
    val channel = new EmbeddedChannel()
    System.err.println("Calling addLast")
    channel
      .pipeline()
      .addLast(new MyHandler())
    System.err.println("Called addLast")
  }
}


class MyHandler()(implicit timer: Timer)
  extends SimpleChannelInboundHandler[Msg] {
  @annotation.nowarn("cat=w-flag-value-discard")
  def sendRequest(ctx: ChannelHandlerContext): Unit = ()
  override def channelRead0(ctx: ChannelHandlerContext, msg: Msg): Unit = ()
}

sealed trait Msg
