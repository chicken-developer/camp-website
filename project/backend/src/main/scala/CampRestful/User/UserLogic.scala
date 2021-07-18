package CampRestful.User

import Routes.Data.{ObjectId, User, templateUser}
import org.mongodb.scala.bson.Document

case object UserLogic {
  def ConvertToUser(jsonStr: String) : User = {
    val user: User = jsonStr
      .replace("User{{","")
      .replace("}}","")
    match {
      case s"_id=$id, username=$username, userType=$usertype, firstName=$f_name, lastName=$l_name, password=$password, email=$email, phoneNumber=$phoneNumber, bookingHistoryId=$arrBooking" =>
        //List(_id=60edea8d21136946402ced6d, username=test, userType=admin, firstName=Test, lastName=Test, password=test123, email=test@gmail.com, phoneNumber=123123123, bookingHistoryId=[b_123123, b_456456])
        val bookingHistory = arrBooking.replace("[", "").replace("]", "").split(",").toList
        User(ObjectId(id), username,usertype, f_name,l_name,password,email,phoneNumber,bookingHistory)
      case _ => templateUser
    }
    user
  }

  def DocumentFromUser(user: User): Document = {
    Document("username" -> user.username, "userType" -> user.typeOfUser,
      "firstName" -> user.firstName, "lastName" -> user.lastName, "password" -> user.password,
      "email"-> user.email, "phoneNumber" -> user.phoneNumber,"bookingHistoryId" -> user.bookingHistoryId)
  }

}