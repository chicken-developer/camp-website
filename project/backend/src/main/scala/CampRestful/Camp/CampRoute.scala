package CampRestful.Camp

import CampRestful.Camp.CampLogic.{CRUDMethodLogic, GetMethodLogic}
import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import spray.json.DefaultJsonProtocol.{StringJsonFormat, listFormat}
import spray.json._

import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}

class CampRoute(implicit val actorSystem : ActorSystem, implicit  val actorMaterializer: Materializer) extends Directives {
  import Routes.Data._

  val campForSpecialDataRoute: Route = {
    (get & path("api_v01" / "home")) {
      val listCampForHomeFuture = GetMethodLogic.GetCampForHomePage()
      onComplete(listCampForHomeFuture) {
        case Success(campsForHome) =>
          complete(
            HttpEntity(
              ContentTypes.`application/json`,
              Message(s"Success with ${campsForHome.size} camps", 1, campsForHome.toJson).toJson.prettyPrint
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
    } ~
    (get & path("api_v01" / "admin")) {
        val camps = GetMethodLogic.GetAllCampData()
        onComplete(camps) {
          case Success(listCamp) =>
            complete(
              HttpEntity(
                ContentTypes.`application/json`,
                Message(s"Success with ${listCamp.size}", 1, listCamp.toJson).toJson.prettyPrint
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
  }

  val CRUDCampRoute: Route = {
    pathPrefix("api_v01" / "camp" / Segment) { campId =>
      get {
          val camp = GetMethodLogic.GetFullCampDataById(campId)
          onComplete(camp) {
            case Success(aCamp) =>
              if (aCamp._id == templateCampData._id) { //TODO
                complete(
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message(s"Success with camp id ${aCamp._id}", 1, aCamp.toJson).toJson.prettyPrint
                  )
                )
              } else {
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("No have camp", 0, "".toJson).toJson.prettyPrint
                  )
                )
              }
            case Failure(ex) =>
              complete(
                StatusCodes.InternalServerError,
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message("Fail to get camp", 0, "".toJson).toJson.prettyPrint
                )
              )
          }
        } ~
      (put & extractRequest) { request =>
            val entity = request.entity
            val strictEntityFuture = entity.toStrict(2 seconds)
            val newCampData = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[CampData])
            println(s"Data from server: $newCampData")
            onComplete(newCampData) {
              case Failure(ex) =>
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Some input not valid", 0, "".toJson).toJson.prettyPrint
                  )
                )
              case Success(camp) =>
                val aCamp = CRUDMethodLogic.UpdateCampFromDatabase(camp, campId)
                complete {
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Success", 1, aCamp.toJson).toJson.prettyPrint
                  )
                }
            }
          } ~
      (post & extractRequest) { request =>
            val entity = request.entity
            val strictEntityFuture = entity.toStrict(2 seconds)
            val newCampData = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[CampData])
            println(s"Data from server: $newCampData")
            onComplete(newCampData) {
              case Failure(ex) =>
                complete(
                  StatusCodes.InternalServerError,
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Some input not valid", 0, "".toJson).toJson.prettyPrint
                  )
                )
              case Success(camp) =>
                val aCamp = CRUDMethodLogic.InsertNewCampToDatabase(camp)
                complete {
                  HttpEntity(
                    ContentTypes.`application/json`,
                    Message("Success", 1, aCamp.toJson).toJson.prettyPrint
                  )
                }
            }
          } ~
      delete {
            complete(
              StatusCodes.InternalServerError,
              HttpEntity(
                ContentTypes.`application/json`,
                Message("Success", 1, "".toJson).toJson.prettyPrint
              )
            )
          }
      }
    }

  val campFinalRoute = campForSpecialDataRoute ~ CRUDCampRoute
}
