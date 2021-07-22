package CampRestful.User

import Routes.Toolkit
import Routes.Toolkit.system.dispatcher
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import com.fasterxml.jackson.annotation.JsonFormat
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class UserRoute(implicit val actorSystem : ActorSystem, implicit  val actorMaterializer: Materializer) extends Directives{
  import Routes.Data._
  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
  val userFinalRoute: Route = cors() {
    pathPrefix("api_v01" / "user") {
        (post & pathPrefix("login") & extractRequest) { request =>
          //POST user -> for user register
          case class UserForm(username: String, password: String)
          implicit val userFormFormat = jsonFormat2(UserForm)
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val loginForm = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[UserForm])
          println(s"Data from server: $loginForm")

          onComplete(loginForm) {
            case Failure(ex) =>
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message(ex.toString, 0, "".toJson).toJson.prettyPrint
                );
                StatusCodes.InternalServerError
              }
            case Success(form) => //Write user to database if valid
              val userFuture = Toolkit.GetUserFromLoginForm(form.username, form.password)
              onComplete(userFuture) {
                case Failure(ex) =>
                  complete {
                    HttpEntity(
                      ContentTypes.`application/json`,
                      Message(ex.toString, 0, "".toJson).toJson.prettyPrint
                    );
                    StatusCodes.InternalServerError
                  }
                case Success(confirmUser) =>
                  if(confirmUser.username == templateUser.username)
                    {
                      complete {
                        HttpEntity(
                          ContentTypes.`application/json`,
                          Message("", 0, "".toJson).toJson.prettyPrint
                        );
                        StatusCodes.InternalServerError
                      }
                    }
                  else
                    {
                      complete {
                        HttpEntity(
                          ContentTypes.`application/json`,
                          Message("", 1, confirmUser.toJson).toJson.prettyPrint
                        )
                      }
                    }

              }
          }
        } ~
        (post & pathPrefix("register") & extractRequest) { request =>
            case class RegisterForm(username: String,firstName: String, lastName: String, password: String, email: String, phoneNumber: String)
            implicit val registerFormFormat = jsonFormat6(RegisterForm)
            val entity = request.entity
            val strictEntityFuture = entity.toStrict(2 seconds)
            val registerForm = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[RegisterForm])
            println(s"Data from server: $registerForm")

            onComplete(registerForm) {
              case Failure(ex) =>
                complete {
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message(ex.toString, 0, "".toJson).toJson.prettyPrint
                  );
                  StatusCodes.InternalServerError
                }
              case Success(form) => //Write user to database if valid
                val user: User = User("id_null",form.username,"normal", form.firstName, form.lastName, form.password,form.email, form.phoneNumber, List(""))
                val userFuture = Toolkit.WriteUserToDatabase(user)
                onComplete(userFuture) {
                  case Failure(ex) =>
                    complete {
                      HttpEntity(
                        ContentTypes.`application/json`,
                        Message(ex.toString, 0, "".toJson).toJson.prettyPrint
                      );
                      StatusCodes.InternalServerError
                    }
                  case Success(confirmUser) =>
                    complete {
                      HttpEntity(
                        ContentTypes.`application/json`,
                        Message("", 1, user.toJson).toJson.prettyPrint
                      )
                    }
                }
            }
          } ~
        (get &  pathPrefix("all")) {
          val userFuture = Toolkit.GetAllUser()
          onComplete(userFuture) {
            case Success(user) =>
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message("",0,user.toJson).toJson.prettyPrint
                )
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (put & pathPrefix(Segment) & extractRequest) { (username, request) =>
          //PUT user -> for admin update user information
          val entity = request.entity
          val newUserInfoFuture = Toolkit.FindUserInDatabase(entity)

          onComplete(newUserInfoFuture) {
            case Success(newUser) => //Delete user from database if valid
              val statusCode = Toolkit.UpdateUserInformation(newUser, newUser.username)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (get & pathPrefix(Segment) & pathPrefix("history")) { userid =>
        //GET booking history of user using their username -> for admin handle user booking history
          val bookingListFuture = Toolkit.GetUserById(userid)
          onComplete(bookingListFuture) {
            case Success(allBookingHistory) =>
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  allBookingHistory.toJson.prettyPrint
                )
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (post & pathPrefix(Segment) & pathPrefix("booking") & extractRequest) { (username,request) =>
        // POST booking -> for user booking a camp
          val entity = request.entity
          val bookingFuture = Toolkit.GenerateBookingFromHttpEntity(entity)

          onComplete(bookingFuture) {
            case Success(booking) => //Write user to database if valid
              val statusCode = Toolkit.WriteBookingToDatabase(booking)
              onComplete(statusCode) {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        }
    }// Finish user path prefix
  }// Finish user route
}
