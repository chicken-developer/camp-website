package CampRestful.Camp.CampLogic

import CampRestful.Camp.CampLogic.GetMethodLogic._
import Routes.Data._
import org.mongodb.scala.bson.Document
import spray.json.{JsValue, enrichAny}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case object CampConverter {

  def ConvertToCamp(jsonStr: String): Camp = {
    val camp: Camp = jsonStr
      .replace("Camp{{", "")
      .replace("}}", "")
    match {
      case s"_id=$id, allowableEquipmentListId=$ae_id, allowableVehicleAndDrivewayDetailsId=$av_id, campImgSrc=$campImgSrcList, campLocationAddress=$location, campName=$name, nearAddress=$nearAddr, partAddress=$partAddr, price=$price, siteAvailabilityId=$sa_id, siteDetailsId=$sd_id" =>
        val imgSources = campImgSrcList.replace("[", "").replace("]", "").split(",").toList
        Camp(_id = id, campName = name, price = price.toDouble, campImgSrc = imgSources, partAddress = partAddr, nearAddress = nearAddr, campLocationAddress = location, siteAvailabilityId = sa_id, siteDetailsId = sd_id, allowableEquipmentListId = ae_id, allowableVehicleAndDrivewayDetailsId = av_id)
      case _ => templateCamp
    }
    camp
  }

  def ConvertToCampForHomePage(c: Camp): CampForHomePage = {
    val sd = GetSiteDetailsById(c.siteDetailsId)
    val ae = GetAllowableEquipmentById(c.allowableEquipmentListId)
    val vd = GetAllowableVehicleAndDrivewayDetailsById(c.allowableVehicleAndDrivewayDetailsId)

    val sd_typeOfUse: String = sd.typeOfUse
    val sd_maxNumOfPeople = sd.maxNumOfPeople
    val vd_maxNumOfVehicles = vd.maxNumOfVehicles
    val vd_maxVehicleLengthForVehicle = vd.maxVehicleLength
    println("========DEBUG+===========")
    println(ae)
    println(ae.items)
    println("========DEBUG+===========")

    val rvMax = ae.items.find(_._1 == "RV").get._2
    val tenMax = ae.items.find(_._1 == "Trailer").get._2
    CampForHomePage(c._id, c.campName, c.campImgSrc.head, c.campImgSrc, c.campLocationAddress,sd_typeOfUse,sd_maxNumOfPeople, vd_maxNumOfVehicles,vd_maxVehicleLengthForVehicle,rvMax.toDouble, tenMax.toDouble, c.price)
  }

  def ConvertToCampData(c: Camp): CampData = {
    val sa = GetSiteAvailabilityById(c.siteAvailabilityId)
    val sd = GetSiteDetailsById(c.siteDetailsId)
    val ae = GetAllowableEquipmentById(c.allowableEquipmentListId)
    val vd = GetAllowableVehicleAndDrivewayDetailsById(c.allowableVehicleAndDrivewayDetailsId)
    CampData(c._id,c.campName, c.price, c.campImgSrc, c.partAddress, c.nearAddress, c.campLocationAddress, sa.toJson, sd.toJson, ae.toJson, vd.toJson)
  }

  def DocumentFromCamp(c: Camp): Document = {
    Document(
      "allowableEquipmentListId" -> c.allowableEquipmentListId,
      "allowableVehicleAndDrivewayDetailsId" -> c.allowableVehicleAndDrivewayDetailsId,
      "campImgSrc" -> c.campImgSrc,
      "campLocationAddress" -> c.campLocationAddress,
      "campName" -> c.campName,
      "nearAddress" -> c.nearAddress,
      "partAddress" -> c.partAddress,
      "price" -> c.price,
      "siteAvailabilityId" -> c.siteAvailabilityId,
      "siteDetailsId" -> c.siteDetailsId
    )
  }

  def ConvertToSiteDetails(jsonStr: String): SiteDetails = {
    val aSiteDetails: SiteDetails = jsonStr
      .replace("SiteDetails{{", "")
      .replace("}}", "")
    match {
      case s"_id=$id, campFireAllowed=$campFireAllowed, capacityRating=$capacityRating, checkInTime=$checkInTime, checkOutTime=$checkOutTime, maxNumOfPeople=$maxNumOfPeople, minNumOfPeople=$minNumOfPeople, petAllowed=$petAllowed, shade=$shade, siteAccessible=$siteAccessible, siteReserveType=$siteReserveType, siteType=$siteType, typeOfUse=$typeOfUse" =>
        SiteDetails(id, siteType, siteAccessible, checkInTime, checkOutTime, maxNumOfPeople.toDouble, minNumOfPeople.toDouble, typeOfUse, siteReserveType, capacityRating, campFireAllowed, petAllowed, shade)
      case _ => templateSiteDetails
    }
    aSiteDetails
  }

  def ConvertToAllowableEquipment(jsonStr: String): AllowableEquipment = {
    val campAllowableEquipment: AllowableEquipment = jsonStr
      .replace("AllowableEquipment{{", "")
      .replace("}}", "")
    match {
      case s"_id=$id, items=$items" =>
        val objList = items.replace(" ", "").split(',').toList
          .map { obj =>
            println(s"$obj")
            val temp = obj.split('=').toList
            println(s"$temp")
            val result = Map(temp.head -> temp.tail.head)
            println(s"$result")
            result
          }.flatten.toMap[String, String]
        println(s"Object map: $objList")
        println(s"ae value map: ${AllowableEquipment(id, objList)}")
        AllowableEquipment(id, objList)

      case _ => templateAllowableEquipment
    }
    campAllowableEquipment
  }

  def ConvertToSiteAvailability(jsonStr: String): SiteAvailability = {
    val campSiteAvailability: SiteAvailability = jsonStr
      .replace("SiteAvailability{{", "")
      .replace("}}", "")
    match {
      case s"_id=$id, date=$date, state=$state " =>
        SiteAvailability(id, date, state)
      case _ => templateSiteAvailability
    }
    campSiteAvailability
  }

  def ConvertToAllowableVehicleAndDrivewayDetails(jsonStr: String): AllowableVehicleAndDrivewayDetails = {
    val campVehicleDetails: AllowableVehicleAndDrivewayDetails = jsonStr
      .replace("AllowableVehicleAndDrivewayDetails{{", "")
      .replace("}}", "")
    match {
      case s"_id=$id, drivewayEntry=$drivewayEntry, drivewayLength=$drivewayLength, drivewaySurface=$drivewaySurface, isEquipmentMandatory=$isEquipmentMandatory, maxNumOfVehicles=$maxNumOfVehicles, maxVehicleLength=$maxVehicleLength, siteLength=$siteLength" =>
        AllowableVehicleAndDrivewayDetails(id, drivewayEntry, drivewayLength.toDouble, drivewaySurface, isEquipmentMandatory, maxNumOfVehicles.toDouble, maxVehicleLength.toDouble, siteLength.toDouble)
      case _ => templateAllowableVehicleAndDrivewayDetails
    }
    campVehicleDetails
  }
}
