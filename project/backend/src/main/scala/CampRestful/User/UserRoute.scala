package CampRestful.User

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class UserRoute(implicit val actorSystem : ActorSystem, implicit  val actorMaterializer: Materializer) extends Directives{
  import Routes.Data._
  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
  val userRoute: Route = cors() {
    (post & path("api_v01" / "user" / "login") & extractRequest) { request =>
      case class UserForm(username: String, password: String)
      implicit val userFormFormat = jsonFormat2(UserForm)
      val entity = request.entity
      val strictEntityFuture = entity.toStrict(2 seconds)
      val loginForm = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[UserForm])
      println(s"Data from server: $loginForm")

      onComplete(loginForm) {
        case Failure(ex) =>
          complete(
            StatusCodes.InternalServerError,
            HttpEntity(
              ContentTypes.`application/json`,
              Message("Input not valid", 0, "".toJson).toJson.prettyPrint
            )
          )
        case Success(form) => //Write user to database if valid
          val userFuture = UserLogic.GetUserFromLoginForm(form.username, form.password)
          onComplete(userFuture) {
            case Failure(ex) =>
              complete(
                StatusCodes.InternalServerError,
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message("Can't handle input", 0, "".toJson).toJson.prettyPrint
                )
              )
            case Success(confirmUser) =>
              if (confirmUser.username == templateUser.username) {
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Wrong username or password", 0, "".toJson).toJson.prettyPrint
                  )
                )
              }
              else {
                complete {
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Success", 1, confirmUser.toJson).toJson.prettyPrint
                  )
                }
              }

          }
      }
    } ~
    (post & path("api_v01" / "user" / "register") & extractRequest) { request =>
        case class RegisterForm(username: String, firstName: String, lastName: String, password: String, email: String, phoneNumber: String)
        implicit val registerFormFormat = jsonFormat6(RegisterForm)
        val entity = request.entity
        val strictEntityFuture = entity.toStrict(2 seconds)
        val registerForm = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[RegisterForm])
        println(s"Data from server: $registerForm")

        onComplete(registerForm) {
          case Failure(ex) =>
            complete(
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message("Input not valid", 0, "".toJson).toJson.prettyPrint
              )
            )
          case Success(form) => //Write user to database if valid
            val user: User = User("id_null", form.username, "normal", form.firstName, form.lastName, form.password, form.email, form.phoneNumber, List(""))
            val userFuture = UserLogic.HandleUserRegister(user)
            onComplete(userFuture) {
              case Failure(ex) =>
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Fail to register", 0, "".toJson).toJson.prettyPrint
                  )
                )
              case Success(valueNotUse) =>
                complete {
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Success", 1, user.toJson).toJson.prettyPrint
                  )
                }
            }
        }
      } ~
    (put & path("api_v01" / "user" / "update" / Segment) & extractRequest) { (userId, request) =>
        case class NewData(username: String, firstName: String, lastName: String, password: String, email: String, phoneNumber: String)
        implicit val updateFormat = jsonFormat6(NewData)
        val entity = request.entity
        val strictEntityFuture = entity.toStrict(2 seconds)
        val newDataForm = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[NewData])
        println(s"Data from server: $newDataForm")

        onComplete(newDataForm) {
          case Failure(ex) =>
            complete(
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message("New data not valid", 0, "".toJson).toJson.prettyPrint
              )
            )
          case Success(form) => //Write user to database if valid
            val userDataTemplate: User = User(userId, form.username, "normal", form.firstName, form.lastName, form.password, form.email, form.phoneNumber, List(""))
            val userFuture = UserLogic.HandleUserUpdateData(userDataTemplate, userId)
            onComplete(userFuture) {
              case Failure(ex) =>
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Fail to update", 0, "".toJson).toJson.prettyPrint
                  )
                )
              case Success(userWithNewData) =>
                if(userWithNewData._id == templateUser._id ) {
                  complete {
                    HttpEntity(
                      ContentTypes.`application/json`,
                      Message("Not have user", 0, "".toJson).toJson.prettyPrint
                    )
                  }
                } else {
                  complete {
                    HttpEntity(
                      ContentTypes.`application/json`,
                      Message("Success", 1, userWithNewData.toJson).toJson.prettyPrint
                    )
                  }
                }
            }
        }
      } ~
    (delete & path("api_v01" / "user" / Segment)) { userId =>
      val userFuture = UserLogic.HandleDeleteUser(userId)
      onComplete(userFuture) {
        case Success(user) =>
          complete {
            HttpEntity(
              ContentTypes.`application/json`,
              Message("Success", 1, "".toJson).toJson.prettyPrint
            )
          }
        case Failure(ex) =>
          complete(
            StatusCodes.InternalServerError,
            HttpEntity(
              ContentTypes.`application/json`,
              Message("Fail to delete", 0, "".toJson).toJson.prettyPrint
            )
          )
      }
    } ~
    (get & path("api_v01" / "user" / Segment)) { userId =>
      userId match {
        case "all" =>
          val userFuture = UserLogic.GetAllUser()
          onComplete(userFuture) {
            case Success(user) =>
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message(s"Success get ${user.size}", 1, user.toJson).toJson.prettyPrint
                )
              }
            case Failure(ex) =>
              complete(
                StatusCodes.InternalServerError,
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message("Fail to get all user", 0, "".toJson).toJson.prettyPrint
                )
              )
          }
        case _ =>
          val user = UserLogic.GetUserById(userId)
          onComplete(user) {
            case Failure(ex) =>
              complete(
                StatusCodes.InternalServerError,
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message(s"Not have user with _id: $userId", 0, "".toJson).toJson.prettyPrint
                )
              )
            case Success(confirmUser) =>
              if (confirmUser.username == templateUser.username) {
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("User data not valid", 0, "".toJson).toJson.prettyPrint
                  )
                )
              }
              else {
                complete {
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Success", 1, confirmUser.toJson).toJson.prettyPrint
                  )
                }
              }
          }
      }
      }
    }
  val historyRoute: Route = cors() {
    pathPrefix("api_v01" / "history") {
      (get & pathPrefix(Segment)) { userid =>
        val historyBookingOfUser = BookingLogic.GetAllBookingForHistory(userid)
        onComplete(historyBookingOfUser) {
          case Success(allBookingHistory) =>
            val finalData = allBookingHistory.filter(booking => booking._id != templateBooking._id)
            complete {
              HttpEntity(
                ContentTypes.`application/json`,
                Message("Success", 1, finalData.toJson).toJson.prettyPrint
              )
            }
          case Failure(ex) =>
            complete(
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message("Fail to get history", 0, "".toJson).toJson.prettyPrint
              )
            )
        }
      }
    }
  }
  val bookingRoute: Route = cors() {
    pathPrefix("api_v01"/ "booking") {
      (post & pathPrefix(Segment) & extractRequest ) { (username, request) =>
        val entity = request.entity
        val strictEntityFuture = entity.toStrict(2 seconds)
        val bookingData = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[Booking])
        println(s"Data from server: $bookingData")

        onComplete(bookingData) {
          case Failure(ex) =>
            complete(
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message("New booking data not valid", 0, "".toJson).toJson.prettyPrint
              )
            )
          case Success(form) => //Write user to database if valid
            val result = BookingLogic.WriteBookingToDatabase(form)
            onComplete(result) {
              case Failure(ex) =>
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Fail to write in database", 0, "".toJson).toJson.prettyPrint
                  )
                )
              case Success(booking) =>
                complete {
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Success", 1, booking.toJson).toJson.prettyPrint
                  )
                }
            }
        }
      }
    }
  }
  val userFinalRoute = cors(){ userRoute ~ historyRoute ~ bookingRoute }
}
