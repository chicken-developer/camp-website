package Routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class UserRoute extends AccountJsonProtocol {

  implicit val system = ActorSystem("AccountServer")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  /**
   * User Http Requests:
   *
   * - GET /user: retrieve ALL the people you have registered
   * - GET /api/people/pin: retrieve the person with that PIN, return as JSON
   * - GET /api/people?pin=X (same)
   * - (harder) POST /api/people with a JSON payload denoting a Person, add that person to your database
   *   - extract the HTTP request's payload (entity)
   *     - extract the request
   *     - process the entity's data
   */
  import Data._
  val finalRoute =
    pathPrefix("api" / "user") {
        (get & parameter("userName")) { userName =>
          val user = User("username", "typeOfUser", "firstName", "lastName", "password", "email", "phoneNumber", List("bookingHistoryID1","bookingHistoryID2"))
          complete (
            HttpEntity(
              ContentTypes.`application/json`,
              user.toJson.prettyPrint
            )
          )

        } ~
          pathEndOrSingleSlash {
            val user = User("username", "typeOfUser", "firstName", "lastName", "password", "email", "phoneNumber", List("bookingHistoryID1","bookingHistoryID2"))
            complete (
              HttpEntity(
                ContentTypes.`application/json`,
                user.toJson.prettyPrint
              )
            )
          }
      } ~
        (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) =>
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val userFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[Data.User])

          onComplete(userFuture) {
            case Success(account) =>
              log.info(s"Got user: $userFuture")
              //TODO: Write to database
              complete(StatusCodes.OK)
            case Failure(ex) =>
              failWith(ex)
          }
        }
}