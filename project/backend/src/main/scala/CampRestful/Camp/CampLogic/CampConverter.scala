package CampRestful.Camp.CampLogic

import Routes.Data._
import org.mongodb.scala.bson.Document

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
        SiteDetails(id, siteType, siteAccessible, checkInTime, checkOutTime, maxNumOfPeople.toInt, minNumOfPeople.toInt, typeOfUse, siteReserveType, capacityRating, campFireAllowed, petAllowed, shade)
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
        val itemList: Map[String, String] = Map()
        val objList = items.split(',').toList
          .map { obj =>
            val temp = obj.split(':').toList
            val result = Map(temp.head -> temp.tail.head)
            result
          }
        objList.foreach(obj => itemList ++ obj)

        AllowableEquipment(id, itemList)
      case _ => templateAllowableEquipment
    }
    campAllowableEquipment
  }

  def ConvertToSiteAvailability(jsonStr: String): SiteAvailability = {
    val campSiteAvailability: SiteAvailability = jsonStr
      .replace("SiteAvailability{{", "")
      .replace("}}", "")
    match {
      case s"_id=$id, date=$date,state=$state " =>
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
        AllowableVehicleAndDrivewayDetails(id, drivewayEntry, drivewayLength.toDouble, drivewaySurface, isEquipmentMandatory, maxNumOfVehicles.toInt, maxVehicleLength.toInt, siteLength.toInt)
      case _ => templateAllowableVehicleAndDrivewayDetails
    }
    campVehicleDetails
  }
}
