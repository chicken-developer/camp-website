package Routes

import CampServer.MongoConfig.MongoController
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.{HttpEntity, StatusCode}
import akka.stream.Materializer
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.enrichAny

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


/*

 */
import MongoController._
case object Collections {
  val campCollection = GetCollection("Camp")
  val userCollection = GetCollection("User")
  val bookingCollection = GetCollection("Booking")
  val campSiteDetailsCollection = GetCollection("CampSiteDetails")
  val campAllowableEquipmentCollection = GetCollection("CampAllowableEquipment")
  val campSiteAvailabilityCollection = GetCollection("CampSiteAvailability")
  val campVehicleDetailsCollection = GetCollection("CampVehicleDetails")
}

case object RouteLogic {
  import Collections._
  import Core.Data._
  implicit val system = ActorSystem("AccountServer")
  implicit val materializer = Materializer
  import system.dispatcher

  def GetAllCamp(): Future[List[Camp]] = {
    val allCamps = campCollection.find()
      .map { camp =>
        camp.toString.toJson
      }.toList
    println("LOG_CAMP")
    println(allCamps)
    println("LOG_CAMP")

    val temp = Future(List[Camp](templateCamp))
    temp
  }

  def GetCampById(id: String): Future[Camp] = {

    //TODO
    val temp = Future(List[Camp](templateCamp).head)
    temp
  }

  def GetUserById(id: String): Future[User] = {

    //TODO
    val temp = Future(List[User](templateUser).head)
    temp
  }
  def GetLimitUser(numberOfUsers: String): Future[List[User]] = {

    //TODO
    val temp = Future(List[User](templateUser))
    temp
  }

  def GetBookingHistoryFromUser(id: String): Future[List[Booking]] = {

    //TODO
    val temp = Future(List[Booking](templateBooking))
    temp
  }

  def GenerateUserFromHttpEntity(entity: HttpEntity): Future[User] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[User](templateUser).head)
    temp
  }

  def WriteUserToDatabase(user: User): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def GenerateBookingFromHttpEntity(entity: HttpEntity): Future[Booking] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[Booking](templateBooking).head)
    temp
  }

  def WriteBookingToDatabase(booking: Booking): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def FindUserInDatabase(entity: HttpEntity): Future[User] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[User](templateUser).head)
    temp
  }

  def DeleteUserFromDatabase(user: User): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def UpdateUserInformation(newUser: User, oldUserID: String): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def FindCampInDatabase(entity: HttpEntity): Future[Camp] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[Camp](templateCamp).head)
    temp
  }

  def DeleteCampFromDatabase(camp: Camp): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def UpdateCampInformation(newCamp: Camp, oldCampId: String): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def GenerateCampFromHttpEntity(entity: HttpEntity): Future[Camp] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[Camp](templateCamp).head)
    temp
  }

  def WriteCampToDatabase(camp: Camp): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }
}
