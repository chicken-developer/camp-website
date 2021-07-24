package CampRestful.Camp.CampLogic

import CampRestful.Camp.CampLogic.CampConverter.{ConvertToAllowableEquipment, ConvertToAllowableVehicleAndDrivewayDetails, ConvertToCamp, ConvertToSiteAvailability, ConvertToSiteDetails}
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
      val temp = Future(List(templateCampForHomePage, templateCampForHomePage, templateCampForHomePage, templateCampForHomePage,templateCampForHomePage,templateCampForHomePage,templateCampForHomePage))
      temp
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

  def GetSiteAvailabilityById(id: String): Future[SiteAvailability] = {
    val allSiteAvailability = siteAvailabilityCollection.find()
      .map { siteAvailability =>
        ConvertToSiteAvailability(siteAvailability.toString.replaceAll("Document", "SiteAvailability"))
      }.toList
    val siteAvailability = allSiteAvailability.findLast(_._id == id)
    siteAvailability match {
      case Some(aSiteAvailability) =>
        Future(aSiteAvailability)
      case None => Future(templateSiteAvailability)
    }
  }

  def GetSiteDetailsById(id: String): Future[SiteDetails] = {
    val allSiteDetails = siteDetailsCollection.find()
      .map { siteDetails =>
        ConvertToSiteDetails(siteDetails.toString.replaceAll("Document", "SiteDetails"))
      }.toList
    val siteDetails = allSiteDetails.findLast(_._id == id)
    siteDetails match {
      case Some(aSiteDetails) =>
        Future(aSiteDetails)
      case None => Future(templateSiteDetails)
    }
  }

  def GetAllowableEquipmentById(id: String): Future[AllowableEquipment] = {
    val allAllowableEquipment = allowableEquipmentCollection.find()
      .map { allowableEquipment =>
        ConvertToAllowableEquipment(allowableEquipment.toString.replaceAll("Document", "AllowableEquipment"))
      }.toList
    val allowableEquipment = allAllowableEquipment.findLast(_._id == id)
    allowableEquipment match {
      case Some(aAllowableEquipment) =>
        Future(aAllowableEquipment)
      case None => Future(templateAllowableEquipment)
    }
  }

  def GetAllowableVehicleAndDrivewayDetailsById(id: String): Future[AllowableVehicleAndDrivewayDetails] = {
    val allAllowableVehicleAndDrivewayDetails = allowableVehicleAndDrivewayDetailsCollection.find()
      .map { allowableVehicleAndDrivewayDetails =>
        ConvertToAllowableVehicleAndDrivewayDetails(allowableVehicleAndDrivewayDetails.toString.replaceAll("Document", "AllowableVehicleAndDrivewayDetails"))
      }.toList
    val allowableVehicleAndDrivewayDetails = allAllowableVehicleAndDrivewayDetails.findLast(_._id == id)
    allowableVehicleAndDrivewayDetails match {
      case Some(aAllowableVehicleAndDrivewayDetails) =>
        Future(aAllowableVehicleAndDrivewayDetails)
      case None => Future(templateAllowableVehicleAndDrivewayDetails)
    }
  }

}
