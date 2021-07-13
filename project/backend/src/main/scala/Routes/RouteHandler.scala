package Routes


import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.{Materializer}
import spray.json.DefaultJsonProtocol.listFormat
import spray.json._

import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class RouteHandler {

  implicit val system = ActorSystem("AccountServer")
  implicit val materializer = Materializer

  import system.dispatcher
  import Core.Data._

  val campRoute = {
    /*
       *  get camp // show camp for homepage
       *  post camp // create new camp
       *  put camp // update camp
       *  delete camp // delete camp
     */
    pathPrefix("api_v01" / "camp") {
      pathEndOrSingleSlash { //GET camp for home page
        val camp: List[Camp] = List(templateCamp)
        complete(
          HttpEntity(
            ContentTypes.`application/json`,
            camp.toJson.prettyPrint
          )
        )
      } ~
      (get & parameter("campId")) { campId => //GET camp for home page
          complete {
            "Received GET request for order " + campId
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
     */
    pathPrefix("api_v01" / "user") {
        (get & parameter("userId")) { userId => //GET user from ID
          complete {
            "Received GET request for order " + userId
          }
        } ~
        (get & parameter("historyOfUser")) { historyOfUser => //GET user from ID
            complete {
              "Received GET request for order " + historyOfUser
            }
          } ~
        (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // POST user
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val userFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[User])

          onComplete(userFuture) {
            case Success(user) =>
              log.info(s"Got new camp: $user")
              //TODO: Write to database
              complete(StatusCodes.OK)
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
        (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // POST booking
            val entity = request.entity
            val strictEntityFuture = entity.toStrict(2 seconds)
            val bookingCampFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[Booking])

            onComplete(bookingCampFuture) {
              case Success(orderBill) =>
                log.info(s"Got new booking: $orderBill")
                //TODO: Write to database
                complete(StatusCodes.OK)
              case Failure(ex) =>
                failWith(ex)
            }
          }
    }// Finish user path prefix
  }// Finish user route
  val adminRoute = {
    /*
       *  put user // update user information
      *  delete user // delete user
      *  get booking history(user) // get booking history of user - same with user
      *  post campAllowableEquipment(details)
     */
    pathPrefix("api_v01" / "admin") {
      (get & parameter("userId")) { userId => //GET user from ID
        complete {
          "Received GET request for order " + userId
        }
      } ~
      (get & parameter("historyOfUser")) { historyOfUser => //GET user from ID
          complete {
            "Received GET request for order " + historyOfUser
          }
        } ~
      (delete & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // DELETE user
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val userFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[User])

          onComplete(userFuture) {
            case Success(user) =>
              log.info(s"Got new camp: $user")
              //TODO: Write to database
              complete(StatusCodes.OK)
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (put & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // PUT user
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val bookingCampFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[Booking])

          onComplete(bookingCampFuture) {
            case Success(orderBill) =>
              log.info(s"Got new booking: $orderBill")
              //TODO: Write to database
              complete(StatusCodes.OK)
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (delete & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // DELETE camp
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val userFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[User])

          onComplete(userFuture) {
            case Success(user) =>
              log.info(s"Got new camp: $user")
              //TODO: Write to database
              complete(StatusCodes.OK)
            case Failure(ex) =>
              failWith(ex)
          }
        } ~
      (put & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // PUT camp
            val entity = request.entity
            val strictEntityFuture = entity.toStrict(2 seconds)
            val userFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[User])

            onComplete(userFuture) {
              case Success(user) =>
                log.info(s"Got new camp: $user")
                //TODO: Write to database
                complete(StatusCodes.OK)
              case Failure(ex) =>
                failWith(ex)
            }
          } ~
      (post & pathEndOrSingleSlash & extractRequest & extractLog) { (request, log) => // POST Camp
          val entity = request.entity
          val strictEntityFuture = entity.toStrict(2 seconds)
          val bookingCampFuture = strictEntityFuture.map(_.data.utf8String.parseJson.convertTo[Booking])

          onComplete(bookingCampFuture) {
            case Success(orderBill) =>
              log.info(s"Got new booking: $orderBill")
              //TODO: Write to database
              complete(StatusCodes.OK)
            case Failure(ex) =>
              failWith(ex)
          }
        }
    }// Finish admin path prefix

  }// Finish admin route

  val finalRoute = campRoute ~ userRoute ~ adminRoute
}