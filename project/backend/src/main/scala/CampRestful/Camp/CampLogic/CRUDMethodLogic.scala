package CampRestful.Camp.CampLogic

import CampRestful.Camp.CampLogic.CampConverter.ConvertToCamp
import Routes.Data.{AllowableEquipment, AllowableVehicleAndDrivewayDetails, Camp, CampData, SiteAvailability, SiteDetails, allowableVehicleAndDrivewayDetailsFormat, templateCamp}
import Routes.MongoHelper._
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import org.mongodb.scala.model.Filters.equal

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case object CRUDMethodLogic {
  def HandleInsertNewCap(campData: CampData): CampData = {
    val siteAvailability = campData.siteAvailability.convertTo[SiteAvailability]
    val siteDetails = campData.siteDetails.convertTo[SiteDetails]
    val allowableEquipment = campData.allowableEquipmentList.convertTo[AllowableEquipment]
    val allowableVehicleAndDrivewayDetails = campData.allowableVehicleAndDrivewayDetails.convertTo[AllowableVehicleAndDrivewayDetails]
    val camp = Camp( campData._id, campData.campName,campData.price, campData.campImgSrc,campData.partAddress, campData.nearAddress,
      campData.campLocationAddress, siteAvailability._id, siteDetails._id, allowableEquipment._id, allowableVehicleAndDrivewayDetails._id)

    campCollection.insertOne(CampConverter.DocumentFromCamp(camp))
    siteAvailabilityCollection.insertOne(CampConverter.DocumentFromSiteAvailability(siteAvailability))
    siteDetailsCollection.insertOne(CampConverter.DocumentFromSiteDetails(siteDetails))
    allowableEquipmentCollection.insertOne(CampConverter.DocumentFromAllowableEquipment(allowableEquipment))
    allowableVehicleAndDrivewayDetailsCollection.insertOne(CampConverter.DocumentFromAllowableVehicleAndDrivewayDetails(allowableVehicleAndDrivewayDetails))
    Future(StatusCodes.OK)

    campData
  }

  def HandleCampUpdate(campData: CampData, campId: String): CampData = {
    val siteAvailability = campData.siteAvailability.convertTo[SiteAvailability]
    val siteDetails = campData.siteDetails.convertTo[SiteDetails]
    val allowableEquipment = campData.allowableEquipmentList.convertTo[AllowableEquipment]
    val allowableVehicleAndDrivewayDetails = campData.allowableVehicleAndDrivewayDetails.convertTo[AllowableVehicleAndDrivewayDetails]
    val camp = Camp( campId, campData.campName,campData.price, campData.campImgSrc,campData.partAddress, campData.nearAddress,
      campData.campLocationAddress, siteAvailability._id, siteDetails._id, allowableEquipment._id, allowableVehicleAndDrivewayDetails._id)

    campCollection.replaceOne(equal("_id", campId), CampConverter.DocumentFromCamp(camp))
    siteAvailabilityCollection.replaceOne(equal("_id", siteAvailability._id), CampConverter.DocumentFromSiteAvailability(siteAvailability))
    siteDetailsCollection.replaceOne(equal("_id", siteDetails._id), CampConverter.DocumentFromSiteDetails(siteDetails))
    allowableEquipmentCollection.replaceOne(equal("_id", allowableEquipment._id), CampConverter.DocumentFromAllowableEquipment(allowableEquipment))
    allowableVehicleAndDrivewayDetailsCollection.replaceOne(equal("_id", allowableVehicleAndDrivewayDetails._id), CampConverter.DocumentFromAllowableVehicleAndDrivewayDetails(allowableVehicleAndDrivewayDetails))

    campData
  }

  def HandleDeleteCamp(campLocationAddress: String): Future[StatusCode] = {
    val allCamps = campCollection.find()
      .map { camp =>
        ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
      }.toList
    val camp = allCamps.findLast(_.campLocationAddress == campLocationAddress)
    val c = camp match {
      case Some(aCamp) => aCamp
      case None => templateCamp
    }

    campCollection.deleteOne(equal("campLocationAddress", c.campLocationAddress))
    siteAvailabilityCollection.deleteOne(equal("_id", c.siteAvailabilityId))
    siteDetailsCollection.deleteOne(equal("_id", c.siteDetailsId))
    allowableEquipmentCollection.deleteOne(equal("_id", c.allowableEquipmentListId))
    allowableVehicleAndDrivewayDetailsCollection.deleteOne(equal("_id", c.allowableVehicleAndDrivewayDetailsId))

    Future(StatusCodes.OK)
  }
}
