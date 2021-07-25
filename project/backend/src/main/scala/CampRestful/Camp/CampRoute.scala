package CampRestful.Camp

import CampRestful.Camp.CampLogic.{CampController, GetMethodLogic}
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
  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

  val campRoute: Route = cors() {
    pathPrefix("api_v01" / "home") {
      pathEndOrSingleSlash {
        //GET all camps -> for show all camp in home page
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
      }
    } ~
    (post  & extractRequest & pathPrefix("api_v01" / "camp"/ "create")) { request =>
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
            CampController.InsertNewCampToDatabase(camp)
            complete {
              HttpEntity(
                ContentTypes.`application/json`,
                Message("Success", 1, camp.toJson).toJson.prettyPrint
              )
            }
        }
      } ~
    pathPrefix("api_v01" / "camp" / Segment) { campId =>
      get {
        val camp = Future(templateCampData)
        onComplete(camp) {
          case Success(aCamp) =>
            complete (
              HttpEntity(
                ContentTypes.`application/json`,
                Message(s"Success with camp id ${aCamp._id}", 1, aCamp.toJson).toJson.prettyPrint
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
      } ~
      (put  & extractRequest) { request =>
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
              CampController.InsertNewCampToDatabase(camp)
              complete {
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message("Success", 1, camp.toJson).toJson.prettyPrint
                )
              }
          }
      } ~
      (path("upload") & extractLog) { log =>
        // handle uploading files
        // multipart/form-data
        entity(as[Multipart.FormData]) { formdata =>
          // handle file payload
          val partsSource: Source[Multipart.FormData.BodyPart, Any] = formdata.parts

          val filePartsSink: Sink[Multipart.FormData.BodyPart, Future[Done]] = Sink.foreach[Multipart.FormData.BodyPart] { bodyPart =>
            if (bodyPart.name == "myFile") {
              // create a file
              val filename = "src/main/resources/download/" + bodyPart.filename.getOrElse("tempFile_" + System.currentTimeMillis())
              val file = new File(filename)

              log.info(s"Writing to file: $filename")

              val fileContentsSource: Source[ByteString, _] = bodyPart.entity.dataBytes
              val fileContentsSink: Sink[ByteString, _] = FileIO.toPath(file.toPath)

              // writing the data to the file
              fileContentsSource.runWith(fileContentsSink)
            }
          }
          val writeOperationFuture = partsSource.runWith(filePartsSink)
          onComplete(writeOperationFuture) {
            case Success(_) => complete("File uploaded.")
            case Failure(ex) => complete(s"File failed to upload: $ex")
          }
        }
      }



    } // Finish camp path prefix
  } // Finish camp route


  val campFinalRoute = campRoute
}
