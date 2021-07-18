package CampRestful.Camp

import Routes.Toolkit
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol.listFormat
import spray.json._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class CampRoute(implicit val actorSystem : ActorSystem, implicit  val actorMaterializer: Materializer) extends Directives {
  import Routes.Data._
  val campRoute: Route = {

    pathPrefix("api_v01" / "camp") {
      pathEndOrSingleSlash {
        //GET all camps -> for show all camp in home page
        val listCampFuture = Toolkit.GetAllCamp()
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
      (get & parameter("campId")) { campId =>
        //GET a camp -> for user/admin when click on camp
          val campFuture = Toolkit.GetCampById(campId)
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
        } ~
      (delete & pathEndOrSingleSlash & extractRequest) { request =>
        // DELETE camp -> For admin delete a camp
          val entity = request.entity
          val campFuture = Toolkit.FindCampInDatabase(entity)

          onComplete(campFuture) {
            case Success(camp) =>
              val statusCode = Toolkit.DeleteCampFromDatabase(camp)
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
          // PUT camp -> For admin update camp information
          val entity = request.entity
          val newCampInfoFuture = Toolkit.FindCampInDatabase(entity)

          onComplete(newCampInfoFuture) {
            case Success(newCamp) =>
              val statusCode = Toolkit.UpdateCampInformation(newCamp, newCamp.campId)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
        (post & pathEndOrSingleSlash & extractRequest) { request =>
          // POST camp -> For admin create new camp
          val entity = request.entity
          val newCampFuture = Toolkit.GenerateCampFromHttpEntity(entity)

          onComplete(newCampFuture) {
            case Success(newCamp) => //Delete user from database if valid
              val statusCode = Toolkit.WriteCampToDatabase(newCamp)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        }
    } // Finish camp path prefix
  } // Finish camp route

  val campSiteDetailsRoute: Route = {
    pathPrefix("api_v01" / "camp" / "siteDetails") {
        (get & parameter("campId")) { campId =>
          //GET a camp -> for user/admin when click on camp
          val campFuture = Toolkit.GetCampSiteDetailsByCampId(campId)
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
        } ~
        (delete & pathEndOrSingleSlash & extractRequest) { request =>
          // DELETE camp site detail -> For admin using when delete a camp
          val entity = request.entity
          val campSiteDetailsFuture = Toolkit.FindCampSiteDetailsInDatabase(entity)

          onComplete(campSiteDetailsFuture) {
            case Success(campSiteDetails) =>
              val statusCode = Toolkit.DeleteCampSiteDetailsInDatabase(campSiteDetails)
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
          // PUT camp site details -> For admin update camp information
          val entity = request.entity
          val newCampSiteDetailsFuture = Toolkit.GenerateCampSiteDetailsFromHttpEntity(entity)

          onComplete(newCampSiteDetailsFuture) {
            case Success(newCampSiteDetails) =>
              val statusCode = Toolkit.UpdateCampSiteDetails(newCampSiteDetails, newCampSiteDetails.campSiteDetailsId)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
        (post & pathEndOrSingleSlash & extractRequest) { request =>
          // POST camp -> For admin create new camp
          val entity = request.entity
          val newCampSiteDetailsFuture = Toolkit.GenerateCampSiteDetailsFromHttpEntity(entity)

          onComplete(newCampSiteDetailsFuture) {
            case Success(newCampSiteDetails) => //Delete user from database if valid
              val statusCode = Toolkit.WriteCampSiteDetailsToDatabase(newCampSiteDetails)
              onComplete(statusCode)
              {
                case Success(status) => complete(status)
                case Failure(ex) => failWith(ex)
              }
            case Failure(ex) =>
              failWith(ex)
          }
        }
    } // Finish camp site detail path prefix
  } // Finish camp site detail route

  val campAllowableEquipmentRoute: Route = { //TODO
    pathPrefix("api_v01" / "camp" / "allowableEquipment") {
      complete(StatusCodes.OK)
    }
  }

  val campSiteAvailabilityRoute: Route = { //TODO
    pathPrefix("api_v01" / "camp" / "siteAvailability") {
      complete(StatusCodes.OK)
    }
  }

  val campVehicleDetailsRoute: Route = { //TODO
    pathPrefix("api_v01" / "camp" / "vehicleDetails") {
      complete(StatusCodes.OK)
    }
  }

  val campFinalRoute = campRoute ~ campSiteDetailsRoute ~ campAllowableEquipmentRoute ~ campSiteAvailabilityRoute ~ campVehicleDetailsRoute
}
