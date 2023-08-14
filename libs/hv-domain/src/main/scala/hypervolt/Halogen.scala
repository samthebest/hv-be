package hypervolt

//import cats.kernel.Eq
import hypervolt.TestKinesisPut.reqFromBytes
import software.amazon.awssdk.services.kinesis.model.PutRecordsRequestEntry
import upickle.default.{macroRW, ReadWriter => RW}

import java.security.MessageDigest

sealed trait HalogenTelemetry
object HalogenTelemetry {
  implicit val rw: RW[HalogenTelemetry] =
    RW.merge(HalogenLong.rw, HalogenShort.rw, HalogenInt.rw, HalogenFloat.rw, HalogenBytes.rw)

}
final case class HalogenLong(name: String, data: Long) extends HalogenTelemetry
object HalogenLong {
  implicit val rw: RW[HalogenLong] = macroRW
  def getValue(tel: HalogenTelemetry): Option[Long] =
    tel match {
      case HalogenLong(_, data) => Some(data)
      case _                    => None
    }
}
final case class HalogenShort(name: String, data: Short) extends HalogenTelemetry
object HalogenShort {
  implicit val rw: RW[HalogenShort] = macroRW
  def getValue(tel: HalogenTelemetry): Option[Short] =
    tel match {
      case HalogenShort(_, data) => Some(data)
      case _                     => None
    }
}
final case class HalogenInt(name: String, data: Int) extends HalogenTelemetry
object HalogenInt {
  implicit val rw: RW[HalogenInt] = macroRW
  def getValue(tel: HalogenTelemetry): Option[Int] =
    tel match {
      case HalogenInt(_, data) => Some(data)
      case _                   => None
    }
}
final case class HalogenFloat(name: String, data: Float) extends HalogenTelemetry
object HalogenFloat {
  implicit val rw: RW[HalogenFloat] = macroRW
  def getValue(tel: HalogenTelemetry): Option[Float] =
    tel match {
      case HalogenFloat(_, data) => Some(data)
      case _                     => None
    }
}
case class HalogenBytes(name: String, data: Array[Byte]) extends HalogenTelemetry
object HalogenBytes {
  implicit val rw: RW[HalogenBytes] = macroRW
  def getValue(tel: HalogenTelemetry): Option[Array[Byte]] =
    tel match {
      case HalogenBytes(_, data) => Some(data)
      case _                     => None
    }
}

final case class Halogen(
                          schema: Short,
                          commit: Int,
                          // TODO item is being stored as `Int` but should be `Short`. See CLOUD-649
                          itemId: Int,
                          device: Long,
                          timestamp: Long,
                          isRefresh: Boolean,
                          telemetry: HalogenTelemetry,
                        )

object Halogen {
  implicit val rw: RW[Halogen] = macroRW

  implicit class HalogenSyntax(self: Halogen) {
    def toKinesisPutEntry: PutRecordsRequestEntry =
      reqFromBytes(upickle.default.writeBinary(self), s"${self.device}")
  }



}
