package CampRestful.User

import Routes.Data.{Booking, templateBooking}
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, StatusCode, StatusCodes}
import akka.stream.Materializer
import org.mongodb.scala.bson.Document

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

case object BookingLogic {

  import Routes.MongoHelper._
  implicit val system: ActorSystem = ActorSystem("AccountServer")
  implicit val materializer: Materializer.type = Materializer

  def GenerateBookingFromHttpEntity(entity: HttpEntity): Future[Booking] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[Booking](templateBooking).head)
    temp
  }

  def WriteBookingToDatabase(booking: Booking): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def ConvertToBooking(jsonStr: String): Booking = {
    val booking: Booking = jsonStr
      .replace("Booking{{","")
      .replace("}}","")
    match {
      case s"_id=$id,  campBookedId=$arrCampBooked,time=$time, totalPrice=$totalPrice, , usernameBooked=$usernameBooked," =>
        val campBooked = arrCampBooked.replace("[", "").replace("]", "").split(",").toList
        Booking(id, usernameBooked, time, totalPrice.toDouble ,campBooked)
      case _ => templateBooking
    }
    booking
  }

  def DocumentFromBooking(booking: Booking): Document = {
    Document("usernameBooked" -> booking.usernameBooked,
      "time" -> booking.time,
      "totalPrice" -> booking.totalPrice,
      "campBookedId" -> booking.campBookedId
    )
  }
}
