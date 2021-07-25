package CampRestful.User

import Routes.Data._
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, StatusCode, StatusCodes}
import akka.stream.Materializer
import org.bson.types.ObjectId
import org.mongodb.scala.bson.{BsonObjectId, Document}
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.{combine, currentDate, set}

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

case object UserLogic {
  import Routes.MongoHelper._

  implicit val system: ActorSystem = ActorSystem("AccountServer")
  implicit val materializer: Materializer.type = Materializer

  def GetUserFromLoginForm(usrname: String, pass: String): Future[User] = {
    val allUsers = userCollection.find()
      .map { user =>
        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
      }.toList
    val user = allUsers.findLast(_.username == usrname).filter(_.password == pass)
    user match {
      case Some(user) =>
        Future(user)
      case None => Future(templateUser)
    }
  }

  def HandleUserRegister(user: User): Future[StatusCode] = {
    userCollection.insertOne(UserLogic.DocumentFromUser(user))
    Future(StatusCodes.OK)
  }

  def HandleUserUpdateData(newData: User, userId: String): Future[User] = {
    val allUsers = userCollection.find()
      .map { user =>
        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
      }.toList
    val user = allUsers.findLast(_._id == userId) match {
      case Some(usr) =>
        val updateDocument = UserLogic.DocumentFromUserForUpdate(oldUser = usr, newUser = newData)
        userCollection.replaceOne(equal("_id", userId),updateDocument)
        usr
      case None => templateUser
    }
    Future(user)
  }

  def HandleDeleteUser(userId: String): Future[StatusCode] = {
    userCollection.deleteOne(equal("_id", userId))
    Future(StatusCodes.OK)
  }

  def GetAllUser(): Future[List[User]] = {
    val allUsers = userCollection.find()
      .map { user =>
        println("==============================User:")
        println(user.toString.replaceAll("Document", "User"))
        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
      }.toList.filter(user => user._id != templateUser._id)
    Future(allUsers)
  }

  def GetUserById(id: String): Future[User] = {
    val allUsers = userCollection.find()
      .map { user =>
        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
      }.toList
    val user = allUsers.findLast(_._id == id)
    user match {
      case Some(user) =>
        Future(user)
      case None => Future(templateUser)
    }
  }

  def ConvertToUser(jsonStr: String) : User = {
    val user: User = jsonStr
      .replace("User{{","")
      .replace("}}","")
    match {
      case s"_id=$id, username=$username, typeOfUser=$typeOfUser, firstName=$f_name, lastName=$l_name, password=$password, email=$email, phoneNumber=$phoneNumber, bookingHistoryId=$arrBooking" =>
        val bookingHistory = arrBooking.replace("[", "").replace("]", "").split(",").toList
        println(s"After format: ${User(id, username,typeOfUser, f_name,l_name,password,email,phoneNumber,bookingHistory)}")
        User(id, username,typeOfUser, f_name,l_name,password,email,phoneNumber,bookingHistory)
      case _ => templateUser
    }
    user
  }

  def DocumentFromUser(user: User): Document = {
    Document("username" -> user.username,
      "typeOfUser" -> user.typeOfUser,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "password" -> user.password,
      "email"-> user.email,
      "phoneNumber" -> user.phoneNumber,
      "bookingHistoryId" -> user.bookingHistoryId)
  }

  def DocumentFromUserForUpdate(oldUser: User, newUser: User): Document = {
    Document("_id" -> oldUser._id,
      "username" -> newUser.username,
      "typeOfUser" -> newUser.typeOfUser,
      "firstName" -> newUser.firstName,
      "lastName" -> newUser.lastName,
      "password" -> newUser.password,
      "email"-> newUser.email,
      "phoneNumber" -> newUser.phoneNumber,
      "bookingHistoryId" -> oldUser.bookingHistoryId)
  }

}