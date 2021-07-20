package Routes

import CampRestful.Camp.CampLogic.ConvertToCamp
import CampRestful.User.UserLogic
import CampServer.MongoConfig.MongoController
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.{HttpEntity, StatusCode}
import akka.stream.Materializer
import com.mongodb.client.MongoCollection
import org.bson.Document

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


import MongoController._
case object Collections {
  val campCollection: MongoCollection[Document] = GetCollection("Camp")
  val userCollection: MongoCollection[Document] = GetCollection("User")
  val bookingCollection: MongoCollection[Document] = GetCollection("Booking")
  val campSiteDetailsCollection: MongoCollection[Document] = GetCollection("CampSiteDetails")
  val campAllowableEquipmentCollection: MongoCollection[Document] = GetCollection("CampAllowableEquipment")
  val campSiteAvailabilityCollection: MongoCollection[Document] = GetCollection("CampSiteAvailability")
  val campVehicleDetailsCollection: MongoCollection[Document] = GetCollection("CampVehicleDetails")
}

case object Toolkit {
  import Collections._
  import Data._
  implicit val system: ActorSystem = ActorSystem("AccountServer")
  implicit val materializer: Materializer.type = Materializer

  import system.dispatcher

  /*Toolkit for camp route */
  def GetAllCamp(): Future[List[Camp]] = {
    val allCamps = campCollection.find()
    .map { camp =>
        ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
      }.toList
    Future(allCamps)
  }

  def GetCampById(id: String): Future[Camp] = {
    val allCamps = campCollection.find()
      .map { camp =>
        ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
      }.toList
    val temp = Future(List[Camp](templateCamp).head)
    temp
  }

  def FindCampInDatabase(entity: HttpEntity): Future[Camp] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[Camp](templateCamp).head)
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

  def DeleteCampFromDatabase(camp: Camp): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def GetCampSiteDetailsByCampId(id: String): Future[CampSiteDetails] = {
    val allCampSiteDetails = campSiteDetailsCollection.find()
      .map { camp =>
        ConvertToCamp(camp.toString.replaceAll("Document", "CampSiteDetails"))
      }.toList
    val temp = Future(List[CampSiteDetails](templateCampSiteDetails).head)
    temp
  }

  def GenerateCampSiteDetailsFromHttpEntity(entity: HttpEntity): Future[CampSiteDetails] = {
    val strictEntityFuture = entity.toStrict(2 seconds)

    val temp = Future(List[CampSiteDetails](templateCampSiteDetails).head)
    temp
  }

  def FindCampSiteDetailsInDatabase(entity: HttpEntity): Future[CampSiteDetails] = {
    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[CampSiteDetails](templateCampSiteDetails).head)
    temp
  }

  def UpdateCampSiteDetails(newCampSiteDetails: CampSiteDetails, oldId: String): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }

  def WriteCampSiteDetailsToDatabase(camp: CampSiteDetails): Future[StatusCode] = {

    val temp = Future(StatusCodes.OK)
    temp
  }
  def DeleteCampSiteDetailsInDatabase(campSiteDetails: CampSiteDetails): Future[StatusCode] = {
    val temp = Future(StatusCodes.OK)
    temp
  }


  /* Toolkit for user route*/
  def GetUserByInformation(inputUsername: String, inputPassword: String): Future[User] = {

    val allUsers = userCollection.find()
      .map { user =>
        UserLogic.ConvertToUser(user.toString.replaceAll("Document", "User"))
      }.toList

    val user = allUsers.findLast(_.username == inputUsername).filter(_.password == inputPassword)
    user match {
      case Some(user) =>
        Future(user)
      case None => Future(templateUser)
    }
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

    val temp = Future(List[User](templateUser).head)
    temp
  }

  def GetUserFromLoginRequest(entity: HttpEntity): Future[User] = {

    val strictEntityFuture = entity.toStrict(2 seconds)
    val temp = Future(List[User](templateUser).head)
    temp
  }

  def WriteUserToDatabase(user: User): Future[StatusCode] = {
    userCollection.insertOne(UserLogic.DocumentFromUser(user))
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


}
