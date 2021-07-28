package CampRestful.User

import Routes.Data.{Booking, User, templateBooking, templateUser}
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, StatusCode, StatusCodes}
import akka.stream.Materializer
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model.Filters.equal

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

case object BookingLogic {

  import Routes.MongoHelper._
  implicit val system: ActorSystem = ActorSystem("AccountServer")
  implicit val materializer: Materializer.type = Materializer

  def GenerateBookingFromHttpEntity(entity: HttpEntity): Future[Booking] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[Booking](templateBooking).head)
    temp
  }

  def GetAllBookingForHistory(userId: String): Future[List[Booking]] = {
    val allBooking = bookingCollection.find()
      .map { booking =>
        BookingLogic.ConvertToBooking(booking.toString.replaceAll("Document", "Booking"))
      }.toList

    def GetBookingHistoryFromUser(user: User): Future[List[Booking]] = {
      val listBooking = user.bookingHistoryId.map { bookingId =>
        val booking = allBooking.findLast(_._id == bookingId)
        booking match {
          case Some(booking) => booking
          case None => templateBooking
        }
      }.filter(booking => booking._id != templateBooking._id)
      Future(listBooking)
    }

    val allUsers = userCollection.find()
      .map { user =>
        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
      }.toList
    val user = allUsers.findLast(_._id == userId)
      user match {
        case Some(user) =>
          GetBookingHistoryFromUser(user)
        case None => Future(List(templateBooking))
      }
  }

  def WriteBookingToDatabase(booking: Booking): Future[Booking] = {
    bookingCollection.insertOne(BookingLogic.DocumentFromBooking(booking))
    Future(booking)
  }
  def WriteBookingToHistoryOfUser(booking: Booking): Unit = {
//    val userId = booking.usernameBooked
//    val bookingIds = bookingCollection.find()
//      .map { booked =>
//        BookingLogic.ConvertToBooking(booked.toString.replaceAll("Document", "Booking"))
//      }.toList
//    println("==============================Debug:")
//    println(s"==============================Debug: $bookingIds")
//    val bookingId =  bookingIds.find(b => b.usernameBooked == userId)
//      .filter(b => b.timeStart == booking.timeStart)
//      .filter(b => b.timeEnd == booking.timeEnd)
//      .get._id
//    println(s"==============================Debug: $bookingId")
//
//
//    val allUsers = userCollection.find()
//      .map { user =>
//        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
//      }.toList
//    allUsers.findLast(_._id == userId) match {
//      case Some(value) =>
//        println("==============================Debug:")
//        println("==============================Debug:")
//        println("==============================Debug:")
//        val newBooking: List[String] = value.bookingHistoryId.isEmpty match {
//          case true => List(bookingId)
//          case false => value.bookingHistoryId :+ bookingId
//        }
//        val userWithNewData = User(value._id, value.username,value.typeOfUser, value.firstName, value.lastName, value.password, value.email, value.phoneNumber,newBooking)
//        println(newBooking)
//        println(userWithNewData)
//        userCollection.replaceOne(equal("username", value.username),UserLogic.DocumentFromUserForAddBooking(value, userWithNewData))
//      case None =>
//        println("Error when update user booking history, return template user")
//    }
  }
  def ConvertToBooking(jsonStr: String): Booking = {
    val booking: Booking = jsonStr
      .replace("Booking{{","")
      .replace("}}","")
    match {
      case s"_id=$id, usernameBooked=$usernameBooked, timeStart=$timeStart, timeEnd=$timeEnd, totalPrice=$totalPrice, campBookedId=$camp" =>
        Booking(id, usernameBooked, timeStart, timeEnd, totalPrice.toDouble ,camp)
      case _ => templateBooking
    }
    booking
  }

  def DocumentFromBooking(booking: Booking): Document = {
    Document("usernameBooked" -> booking.usernameBooked,
      "timeStart" -> booking.timeStart,
      "timeEnd" -> booking.timeEnd,
      "totalPrice" -> booking.totalPrice,
      "campBookedId" -> booking.campBookedId
    )
  }
}
