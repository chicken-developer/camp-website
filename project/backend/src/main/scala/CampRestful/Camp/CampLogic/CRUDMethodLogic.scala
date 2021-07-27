package CampRestful.Camp.CampLogic

import Routes.Data.{CampData, templateCampData}
import Routes.MongoHelper.userCollection
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import org.mongodb.scala.model.Filters.equal

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case object CRUDMethodLogic {
  def HandleInsertNewCap(campData: CampData): CampData =
  {
    templateCampData
  }
  def HandleCampUpdate(newData: CampData, campId: String): CampData = {
    templateCampData
  }


  def HandleDeleteCamp(userId: String): Future[StatusCode] = {
    userCollection.deleteOne(equal("_id", userId))
    Future(StatusCodes.OK)
  }
}
