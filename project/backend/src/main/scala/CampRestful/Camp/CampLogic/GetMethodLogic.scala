package CampRestful.Camp.CampLogic

import CampRestful.Camp.CampLogic.CampConverter.{ConvertToAllowableEquipment, ConvertToAllowableVehicleAndDrivewayDetails, ConvertToCamp, ConvertToCampData, ConvertToCampForHomePage, ConvertToSiteAvailability, ConvertToSiteDetails}

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.concurrent.Future
import Routes.Data._
import Routes.MongoHelper._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

case object GetMethodLogic {

  def GetAllCamp(): Future[List[Camp]] = {
    val allCamps = campCollection.find()
    .map { camp =>
        ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
      }.toList
    Future(allCamps)
  }

  def GetCampForHomePage(): Future[List[CampForHomePage]] = {
    val allCamps = campCollection.find()
      .map { camp =>
        val c = ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
        ConvertToCampForHomePage(c)
      }.toList
    Future(allCamps)
  }

  def GetCampById(id: String): Future[Camp] = {
    val allCamps = campCollection.find()
      .map { camp =>
        ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
      }.toList
    val camp = allCamps.findLast(_._id == id)
    camp match {
      case Some(aCamp) =>
        Future(aCamp)
      case None => Future(templateCamp)
    }
  }

  def GetAllCampData(): Future[List[CampData]] = {
    val allCamps = campCollection.find()
      .map { camp =>
        val c = ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
        ConvertToCampData(c)
      }.toList
    Future(allCamps)
  }

  def GetFullCampDataById(id: String): Future[CampData] = {
    val camp = campCollection.find()
      .map { camp =>
        val c = ConvertToCamp(camp.toString.replaceAll("Document", "Camp"))
        ConvertToCampData(c)
      }.toList.findLast(c => c._id == id) match {
      case Some(value) => Future(value)
      case None => Future(templateCampData)
    }
    camp
  }

  def GetSiteAvailabilityById(id: String): SiteAvailability = {
    val allSiteAvailability = siteAvailabilityCollection.find()
      .map { siteAvailability =>
        ConvertToSiteAvailability(siteAvailability.toString.replaceAll("Document", "SiteAvailability"))
      }.toList
    val siteAvailability = allSiteAvailability.findLast(_._id == id)
    siteAvailability match {
      case Some(aSiteAvailability) =>
        aSiteAvailability
      case None => templateSiteAvailability
    }
    templateSiteAvailability
  }

  def GetSiteDetailsById(id: String): SiteDetails = {
    val allSiteDetails = siteDetailsCollection.find()
      .map { siteDetails =>
        ConvertToSiteDetails(siteDetails.toString.replaceAll("Document", "SiteDetails"))
      }.toList
    val siteDetails = allSiteDetails.findLast(_._id == id)
    siteDetails match {
      case Some(aSiteDetails) =>
        aSiteDetails
      case None => templateSiteDetails
    }

  }

  def GetAllowableEquipmentById(id: String): AllowableEquipment = {
    val allAllowableEquipment = allowableEquipmentCollection.find()
      .map { allowableEquipment =>
        ConvertToAllowableEquipment(allowableEquipment.toString.replaceAll("Document", "AllowableEquipment"))
      }.toList
    val allowableEquipment = allAllowableEquipment.findLast(_._id == id)
    allowableEquipment match {
      case Some(aAllowableEquipment) =>
        aAllowableEquipment
      case None => templateAllowableEquipment
    }
  }

  def GetAllowableVehicleAndDrivewayDetailsById(id: String): AllowableVehicleAndDrivewayDetails = {
    val allAllowableVehicleAndDrivewayDetails = allowableVehicleAndDrivewayDetailsCollection.find()
      .map { allowableVehicleAndDrivewayDetails =>
        ConvertToAllowableVehicleAndDrivewayDetails(allowableVehicleAndDrivewayDetails.toString.replaceAll("Document", "AllowableVehicleAndDrivewayDetails"))
      }.toList
    val allowableVehicleAndDrivewayDetails = allAllowableVehicleAndDrivewayDetails.findLast(_._id == id)
    allowableVehicleAndDrivewayDetails match {
      case Some(aAllowableVehicleAndDrivewayDetails) =>
        aAllowableVehicleAndDrivewayDetails
      case None => templateAllowableVehicleAndDrivewayDetails
    }
  }

}
