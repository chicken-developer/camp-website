package CampRestful.User

import Routes.Toolkit
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol.listFormat
import spray.json._

import scala.language.postfixOps
import scala.util.{Failure, Success}

class UserRoute(implicit val actorSystem : ActorSystem, implicit  val actorMaterializer: Materializer) extends Directives{
  import Routes.Data._
  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
  val userFinalRoute: Route = cors() {
    pathPrefix("api_v01" / "user") {
      (get & parameter("username") & parameter("password")) { (username, password) =>
        //GET user from username & password -> using for login
        val userFuture = Toolkit.GetUserByInformation(username, password)
        onComplete(userFuture) {
            case Success(user) =>
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  user.toJson.prettyPrint
                )
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
        (post & pathPrefix("login") & extractRequest) { request =>
          //POST user -> for user register
          val entity = request.entity
          val userFuture = Toolkit.GetUserFromLoginRequest(entity)

          onComplete(userFuture) {
            case Success(user) => //Write user to database if valid
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  user.toJson.prettyPrint
                )
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (post & pathPrefix("register") & extractRequest) { request =>
          //POST user -> for user register
          val entity = request.entity
          val userFuture = Toolkit.GenerateUserFromHttpEntity(entity)

          onComplete(userFuture) {
            case Success(user) => //Write user to database if valid
              val statusCode = Toolkit.WriteUserToDatabase(user)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (get & parameter("numberOfUser")) { numberOfUser =>
          //Get n first user from database -> for admin handle user
          val userFuture = Toolkit.GetLimitUser(numberOfUser)
          onComplete(userFuture) {
            case Success(user) =>
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  user.toJson.prettyPrint
                )
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (delete & pathEndOrSingleSlash & extractRequest) { request =>
          //DELETE user -> for admin handle user
        val entity = request.entity
          val userFuture = Toolkit.GenerateUserFromHttpEntity(entity)

          onComplete(userFuture) {
            case Success(user) => //Delete user from database if valid
              val statusCode = Toolkit.DeleteUserFromDatabase(user)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (put & pathEndOrSingleSlash & extractRequest) { request =>
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
      (get & parameter("historyOfUser")) { historyOfUser =>
        //GET booking history of user using their username -> for admin handle user booking history
          val bookingListFuture = Toolkit.GetUserById(historyOfUser)
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
      (post & pathPrefix("booking") & extractRequest) { request =>
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
