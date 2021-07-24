package CampRestful.Camp

import CampRestful.Camp.CampLogic.GetMethodLogic
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol.{StringJsonFormat, listFormat}
import spray.json._

import scala.language.postfixOps
import scala.util.{Failure, Success}

class CampRoute(implicit val actorSystem : ActorSystem, implicit  val actorMaterializer: Materializer) extends Directives {
  import Routes.Data._
  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

  val campRoute: Route = cors(){
    pathPrefix("api_v01" / "home") {
      pathEndOrSingleSlash {
        //GET all camps -> for show all camp in home page
        val listCampForHomeFuture = GetMethodLogic.GetCampForHomePage()
        onComplete(listCampForHomeFuture) {
          case Success(campsForHome) =>
            complete(
              HttpEntity(
                ContentTypes.`application/json`,
                Message(s"Success with ${campsForHome.size} camps", 0, campsForHome.toJson).toJson.prettyPrint
              )
            )
          case Failure(ex) =>
            complete(
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message("Fail to get camp", 0, "".toJson).toJson.prettyPrint
              )
            )
        }
      }
    } ~
      pathPrefix("api_v01" / "camp") {
      pathEndOrSingleSlash {
        //GET all camps -> for show all camp in home page
        val listCampFuture = GetMethodLogic.GetAllCamp()
        onComplete(listCampFuture) {
          case Success(listCamp) =>
            complete (
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message(s"Success with ${listCamp.size} camps", 0, listCamp.toJson).toJson.prettyPrint
              )
            )
          case Failure(ex) =>
            complete (
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message("Fail to get camp", 0, "".toJson).toJson.prettyPrint
              )
            )
        }
      }
//      (delete & pathEndOrSingleSlash & extractRequest) { request =>
//        // DELETE camp -> For admin delete a camp
//          val entity = request.entity
//          val campFuture = Toolkit.FindCampInDatabase(entity)
//
//          onComplete(campFuture) {
//            case Success(camp) =>
//              val statusCode = Toolkit.DeleteCampFromDatabase(camp)
//              onComplete(statusCode)
//              {
//                case Success(status) => complete(status)
//                case Failure(ex) => failWith(ex)
//              }
//            case Failure(ex) =>
//              failWith(ex)
//          }
//        } ~


    } // Finish camp path prefix
  } // Finish camp route


  val campFinalRoute = campRoute
}
