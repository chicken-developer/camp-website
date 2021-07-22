package CampRestful.User

import Routes.Data._
import org.mongodb.scala.bson.Document

case object UserLogic {
  def ConvertToUser(jsonStr: String) : User = {
    val user: User = jsonStr
      .replace("User{{","")
      .replace("}}","")
    match {
      case s"_id=$id, username=$username, userType=$usertype, firstName=$f_name, lastName=$l_name, password=$password, email=$email, phoneNumber=$phoneNumber, bookingHistoryId=$arrBooking" =>
        val bookingHistory = arrBooking.replace("[", "").replace("]", "").split(",").toList
        User(id, username,usertype, f_name,l_name,password,email,phoneNumber,bookingHistory)
      case _ => templateUser
    }
    user
  }

  def DocumentFromUser(user: User): Document = {
    Document("username" -> user.username,
      "userType" -> user.typeOfUser,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "password" -> user.password,
      "email"-> user.email,
      "phoneNumber" -> user.phoneNumber,
      "bookingHistoryId" -> "")
  }

  def ConvertToBooking(jsonStr: String): Booking = {
    val booking: Booking = jsonStr
      .replace("Booking{{","")
      .replace("}}","")
    match {
      case s"_id=$id, bookingId=$bookingId, usernameBooked=$usernameBooked, time=$time, totalPrice=$total_price, campBookedId=$arrCampBooked" =>
        val campBooked = arrCampBooked.replace("[", "").replace("]", "").split(",").toList
        Booking(id, bookingId,usernameBooked, time,total_price.toDouble ,campBooked)
      case _ => templateBooking
    }
    booking
  }

  def DocumentFromBooking(booking: Booking): Document = {
    Document("bookingId" -> booking.bookingId,
      "usernameBooked" -> booking.usernameBooked,
      "time" -> booking.time,
      "totalPrice" -> booking.totalPrice,
      "campBookedId" -> booking.campBookedId
    )
  }
}