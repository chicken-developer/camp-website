package Core

object Converter {
  import Data._
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

  def ConvertToUser(jsonStr: String) : User = {
    val user: User = jsonStr
      .replace("User{{","")
      .replace("}}","")
    match {
      case s"_id=$id, username=$username, userType=$usertype, firstName=$f_name, lastName=$l_name, password=$password, email=$email, phoneNumber=$phoneNumber, bookingHistoryId=$arrBooking" =>
      //List(_id=60edea8d21136946402ced6d, username=test, userType=admin, firstName=Test, lastName=Test, password=test123, email=test@gmail.com, phoneNumber=123123123, bookingHistoryId=[b_123123, b_456456])
        val bookingHistory = arrBooking.replace("[", "").replace("]", "").split(",").toList
        User(ObjectId(id), username,usertype, f_name,l_name,password,email,phoneNumber,bookingHistory)
      case _ => templateUser
    }
      user
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
