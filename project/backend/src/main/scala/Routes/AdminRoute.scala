package Routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}


class AdminRoute extends AccountJsonProtocol {

  implicit val system = ActorSystem("AccountServer")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher


  /**
   * Exercise:
   *
   * - GET /api/people: retrieve ALL the people you have registered
   * - GET /api/people/pin: retrieve the person with that PIN, return as JSON
   * - GET /api/people?pin=X (same)
   * - (harder) POST /api/people with a JSON payload denoting a Person, add that person to your database
   *   - extract the HTTP request's payload (entity)
   *     - extract the request
   *     - process the entity's data
   */

  var accounts = List(
    Account(1, "Alice"),
    Account(2, "Bob"),
    Account(3, "Charlie")
  )

  val finalRoute =
    pathPrefix("api" / "people") {
      get {
        (path(IntNumber) | parameter('pin.as[Int])) { pin =>
          complete(
            HttpEntity(
              ContentTypes.`application/json`,
              accounts.find(_.pin == pin).toJson.prettyPrint
            )
          )
        } ~
          pathEndOrSingleSlash {
            complete(
              HttpEntity(
                ContentTypes.`application/json`,
                accounts.toJson.prettyPrint
              )
            )
          }
      } ~
        (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) =>
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val accountFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[Account])

          onComplete(accountFuture) {
            case Success(account) =>
              log.info(s"Got person: $account")
              accounts = accounts :+ account
              complete(StatusCodes.OK)
            case Failure(ex) =>
              failWith(ex)
          }
        }
    }
}