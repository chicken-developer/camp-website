package CampRestful.Camp.CampLogic

import CampRestful.User.CalenderHandler.HandleRawData
import Routes.Data.Message
import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol._
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps
import scala.util.{Failure, Success}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString

import java.io.File
import java.nio.file.{Files, Paths}
import scala.concurrent.Future

class UploadRoute(implicit val actorSystem : ActorSystem, implicit  val actorMaterializer: Materializer) extends Directives {
  val uploadRoute: Route =
    (path("api_v01" / "choosefile") & get) {
      complete(
        HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            |  <body>
            |    <form action="http://103.153.65.194:54000/api_v01/upload" method="post" enctype="multipart/form-data">
            |      <input type="file" name="image">
            |      <button type="submit">Upload</button>
            |    </form>
            |  </body>
            |</html>
          """.stripMargin
        )
      )
    } ~
    (path("api_v01" / "images" / Segment) & get ) { imageName =>
      val originSource = "src/main/resources/img/" + imageName
      val filePath = "img/" + imageName
      if(Files.exists(Paths.get(originSource))) {
          getFromResource(filePath)
      }
      else {
        complete(
          StatusCodes.InternalServerError,
          HttpEntity(
            ContentTypes.`application/json`,
            Message(s"Images not exits with path: $originSource", 0, "".toJson).toJson.prettyPrint
          )
        )
      }

    } ~
    (path("api_v01"/ "upload") & extractLog) { log =>
        // handle uploading files
        // multipart/form-data
        var filePath = ""
        entity(as[Multipart.FormData]) { formdata =>
          // handle file payload
          val partsSource: Source[Multipart.FormData.BodyPart, Any] = formdata.parts

          val filePartsSink: Sink[Multipart.FormData.BodyPart, Future[Done]] = Sink.foreach[Multipart.FormData.BodyPart] { bodyPart =>
            if (bodyPart.name == "image") {
              // create a file
              val filename = "src/main/resources/img/" + bodyPart.filename.getOrElse("tempFile_" + System.currentTimeMillis())
              val file = new File(filename)

              log.info(s"Writing to file: $filename")
              filePath = filename
              val fileContentsSource: Source[ByteString, _] = bodyPart.entity.dataBytes
              val fileContentsSink: Sink[ByteString, _] = FileIO.toPath(file.toPath)

              // writing the data to the file
              fileContentsSource.runWith(fileContentsSink)
            }
          }

          val writeOperationFuture = partsSource.runWith(filePartsSink)
          onComplete(writeOperationFuture) {
            case Success(_) => complete (
                HttpEntity(
                  ContentTypes.`application/json`,
                  Message("Success", 1, filePath.toJson).toJson.prettyPrint
                )
            )
            case Failure(ex) => complete(s"File failed to upload: $ex")
          }
        }
      }

}
