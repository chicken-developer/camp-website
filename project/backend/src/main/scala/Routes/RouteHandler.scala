package Routes


import akka.actor.ActorSystem
import akka.http.scaladsl.model.ResponseEntity.fromJava
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol.{UnitJsonFormat, listFormat}
import spray.json._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class RouteHandler {

  implicit val system = ActorSystem("AccountServer")
  implicit val materializer = Materializer
  import Core.Data._

  val campRoute = {
    /*
       *  get camp // show camp for homepage
       *  post camp // create new camp
       * //For test
       * api_v01/camp/
       * api_v01/camp/?campId=123
     */
    pathPrefix("api_v01" / "camp") {
      pathEndOrSingleSlash { //GET camp for home page
        val listCampFuture = RouteLogic.GetAllCamp()
        onComplete(listCampFuture) {
          case Success(listCamp) =>
            complete {
              HttpEntity(
                ContentTypes.`application/json`,
                listCamp.toJson.prettyPrint
              )
            }
          case Failure(ex) =>
            failWith(ex)
        }
      } ~
      (get & parameter("campId")) { campId => //GET camp for camp page
        val campFuture = RouteLogic.GetCampById(campId)
        onComplete(campFuture) {
          case Success(camp) =>
            complete {
              HttpEntity(
                ContentTypes.`application/json`,
                camp.toJson.prettyPrint
              )
            }
          case Failure(ex) =>
            failWith(ex)
        }
      }
    } // Finish camp path prefix
  } // Finish camp route

  val userRoute = {
    /*
       *  post user(details) // register
       *  get user(details) // login + show details
       *  get booking history(user) // show history - same with admin
       *  post booking // for book a camp
       * ***
       * //For test
       * api_v01/user/?userId=123
       * api_v01/user/?historyOfUser=123
     */
    pathPrefix("api_v01" / "user") {
        (get & parameter("username") & parameter("password")) { (username, password) => //GET user from ID
          val userFuture = RouteLogic.GetUserByInformation(username, password)
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
        (get & parameter("historyOfUser")) { historyOfUser => //GET user from ID
          val bookingListFuture = RouteLogic.GetUserById(historyOfUser)
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
        (post & pathPrefix("register") & extractRequest) { request => // POST user
          val entity = request.entity
          val userFuture = RouteLogic.GenerateUserFromHttpEntity(entity)

          onComplete(userFuture) {
            case Success(user) => //Write user to database if valid
              val statusCode = RouteLogic.WriteUserToDatabase(user)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
        (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // POST booking
          val entity = request.entity
          val bookingFuture = RouteLogic.GenerateBookingFromHttpEntity(entity)

          onComplete(bookingFuture) {
            case Success(booking) => //Write user to database if valid
              val statusCode = RouteLogic.WriteBookingToDatabase(booking)
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

  val adminRoute = {
    /*
      *  get all user // update user information
      *  put user // update user information
      *  delete user // delete user
      *  get booking history(user) // get booking history of user - same with user
      *  post campAllowableEquipment(details)
     */
    pathPrefix("api_v01" / "admin") {
      (get & parameter("numberOfUser")) { numberOfUser => //GET all user
        val userFuture = RouteLogic.GetLimitUser(numberOfUser)
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
      (delete & pathEndOrSingleSlash & extractRequest) { request => // DELETE user
        val entity = request.entity
        val userFuture = RouteLogic.GenerateUserFromHttpEntity(entity)

        onComplete(userFuture) {
          case Success(user) => //Delete user from database if valid
            val statusCode = RouteLogic.DeleteUserFromDatabase(user)
            onComplete(statusCode)
            {
              case Success(status) => complete(status)
              case Failure(ex) => failWith(ex)
            }
          case Failure(ex) =>
            failWith(ex)
        }
      } ~
      (put & pathEndOrSingleSlash & extractRequest) { request => // PUT user
        val entity = request.entity
        val newUserInfoFuture = RouteLogic.FindUserInDatabase(entity)

        onComplete(newUserInfoFuture) {
          case Success(newUser) => //Delete user from database if valid
            val statusCode = RouteLogic.UpdateUserInformation(newUser, newUser.username)
            onComplete(statusCode)
            {
              case Success(status) => complete(status)
              case Failure(ex) => failWith(ex)
            }
          case Failure(ex) =>
            failWith(ex)
        }
      } ~
      (delete & pathEndOrSingleSlash & extractRequest) { request => // DELETE camp
          val entity = request.entity
          val campFuture = RouteLogic.FindCampInDatabase(entity)

          onComplete(campFuture) {
            case Success(camp) => //Delete user from database if valid
              val statusCode = RouteLogic.DeleteCampFromDatabase(camp)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (put & pathEndOrSingleSlash & extractRequest) { request => // PUT camp
          val entity = request.entity
          val newCampInfoFuture = RouteLogic.FindCampInDatabase(entity)

          onComplete(newCampInfoFuture) {
            case Success(newCamp) => //Delete user from database if valid
              val statusCode = RouteLogic.UpdateCampInformation(newCamp, newCamp.campId)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (post & pathEndOrSingleSlash & extractRequest) { request => // POST camp
          val entity = request.entity
          val newCampFuture = RouteLogic.GenerateCampFromHttpEntity(entity)

          onComplete(newCampFuture) {
            case Success(newCamp) => //Delete user from database if valid
              val statusCode = RouteLogic.WriteCampToDatabase(newCamp)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        }
    }// Finish admin path prefix
  }// Finish admin route

  val adminHelpRoute = {
    /*
      * get/ push/ put/ delete
      * campSiteDetails
      * campAllowableEquipmentFormat
      * campSiteAvailabilityFormat
      * campVehicleDetailsFormat
     */
    pathPrefix("api_v01" / "admin"/ "help") {
      complete(StatusCodes.OK) //TODO
    }// Finish admin path prefix
  }

  val finalRoute = campRoute ~ userRoute ~ adminRoute ~ adminHelpRoute
}