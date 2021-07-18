package CampRestful.Camp

import Routes.Data._

case object CampLogic {
  def ConvertToCamp(jsonStr: String) : Camp = {
    val camp: Camp = jsonStr
      .replace("Camp{{","")
      .replace("}}","")
    match {
      case s"_id=$id, campId=$campId, price=$prince, campImgSrc=$campImgSrc, partAddress=$partAddress, nearAddress=$nearAddress, siteDetailsId=$siteDetail, siteAvailabilityId=$siteAvailability, vehicleDetailsId=$vehicleDetailsId, allowableEquipmentListId=$allowableEquipmentListId" =>
        Camp(ObjectId(id), campId, prince.toDouble, campImgSrc, partAddress, nearAddress, siteDetail, siteAvailability, vehicleDetailsId,allowableEquipmentListId)
      case _ => templateCamp
    }
    camp
  }

  def ConvertToBooking(jsonStr: String): Booking = {
    val booking: Booking = jsonStr
      .replace("Booking{{","")
      .replace("}}","")
    match {
      case s"_id=$id, bookingId=$bookingId, usernameBook=$usernameBook, time=$time, totalPrice=$total_price, campBookedListId=$arrCampBooked" =>
        val campBooked = arrCampBooked.replace("[", "").replace("]", "").split(",").toList
        Booking(ObjectId(id), bookingId,usernameBook, time,total_price.toDouble ,campBooked)
      case _ => templateBooking
    }
    booking
  }

  def ConvertToCampSiteDetails(jsonStr: String): CampSiteDetails = {
    val aCampSiteDetails: CampSiteDetails = jsonStr
      .replace("CampSiteDetails{{","")
      .replace("}}","")
    match {
      case s"_id=$id, campSiteDetailsId=$campSiteDetailsId, siteType=$siteType, siteAccessible=$siteAccessible, checkInTime=$checkInTime, checkOutTime=$checkOutTime, maxNumOfPeople=$maxNumOfPeople, minNumOfPeople=$minNumOfPeople, typeOfUse=$typeOfUse, siteReserveType=$siteReserveType, campFireAllowed=$campFireAllowed, capacityRating=$capacityRating, petAllowed=$petAllowed, shade=$shade"  =>
        CampSiteDetails(ObjectId(id),
          campSiteDetailsId,
          siteType,
          siteAccessible,
          checkInTime,
          checkOutTime,
          maxNumOfPeople.toInt,
          minNumOfPeople.toInt,
          typeOfUse,
          siteReserveType,
          campFireAllowed.toBoolean,
          capacityRating,
          petAllowed.toBoolean,
          shade)
      case _ => templateCampSiteDetails
    }
    aCampSiteDetails
  }

  def ConvertToCampAllowableEquipment(jsonStr: String): CampAllowableEquipment = {
    val campAllowableEquipment: CampAllowableEquipment = jsonStr
      .replace("CampAllowableEquipment{{","")
      .replace("}}","")
    match {
      case s"_id=$id, allowableEquipmentId=$allowableEquipmentId, items=$items" =>
        val itemList: Map[String, String] = Map("123" -> "123") // TODO
        CampAllowableEquipment(ObjectId(id), allowableEquipmentId, itemList)
      case _ => templateCampAllowableEquipment
    }
    campAllowableEquipment
  }

  def ConvertToCampSiteAvailability(jsonStr: String): CampSiteAvailability = {
    val campSiteAvailability: CampSiteAvailability = jsonStr
      .replace("CampSiteAvailability{{","")
      .replace("}}","")
    match {
      case s"_id=$id, campSiteAvailabilityId=$campSiteAvailabilityId, date=$date,state=$state " =>
        CampSiteAvailability(ObjectId(id), campSiteAvailabilityId, date, state)
      case _ => templateCampSiteAvailability
    }
    campSiteAvailability
  }

  def ConvertToCampVehicleDetails(jsonStr: String): CampVehicleDetails = {
    val campVehicleDetails: CampVehicleDetails = jsonStr
      .replace("CampVehicleDetails{{","")
      .replace("}}","")
    match {
      case s"_id=$id, CampVehicleDetailsId=$campVehicleDetailsId, drivewayEntry=$drivewayEntry, drivewayLength=$drivewayLength, drivewaySurface=$drivewaySurface, isEquipmentMandatory=$isEquipmentMandatory, maxNumOfVehicles=$maxNumOfVehicles, maxVehicleLength=$maxVehicleLength, siteLength=$siteLength" =>
        CampVehicleDetails(ObjectId(id),
          campVehicleDetailsId,
          drivewayEntry,
          drivewayLength.toDouble,
          drivewaySurface,
          isEquipmentMandatory,
          maxNumOfVehicles.toInt,
          maxVehicleLength.toInt,
          siteLength.toInt)
      case _ => templateCampVehicleDetails
    }
    campVehicleDetails
  }
}