package CampRestful.User

import Routes.Data._
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpEntity, StatusCode, StatusCodes}
import akka.stream.Materializer
import org.mongodb.scala.bson.Document

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
        println("Receive from Mongodb:")
        println(user)
        println("---------------------")
        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
      }.toList
    println("---------------------")
    println("---------------------")

    println(s"\nResultll: $allUsers \n")
    val user = allUsers.findLast(_.username == usrname).filter(_.password == pass)
    println(s"Resultll: $user")
    user match {
      case Some(user) =>
        Future(user)
      case None => Future(templateUser)
    }
  }

  def HandleUserRegister(user: User): Future[StatusCode] = {
    userCollection.insertOne(UserLogic.DocumentFromUser(user))
    val temp = Future(StatusCodes.OK)
    temp
  }
  def GetAllUser(): Future[List[User]] = {

    //TODO
    val temp = Future(List[User](templateUser))
    temp
  }

  def FindUserInDatabase(entity: HttpEntity): Future[User] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[User](templateUser).head)
    temp
  }

  def UpdateUserInformation(newUser: User, oldUserID: String): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }
  def GetUserById(id: String): Future[User] = {

    //TODO
    val temp = Future(List[User](templateUser).head)
    temp
  }
  def ConvertToUser(jsonStr: String) : User = {
    val user: User = jsonStr
      .replace("User{{","")
      .replace("}}","")
    match {
      case s"_id=$id, bookingHistoryId=$arrBooking, email=$email, firstName=$f_name, lastName=$l_name, password=$password, phoneNumber=$phoneNumber, typeOfUser=$typeOfUser, username=$username" =>
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


}