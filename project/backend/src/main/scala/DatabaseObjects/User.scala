package DatabaseObjects

case object Data {
  case class User(username: String, typeOfUser: String, firstName: String, lastName: String, password: String, email: String, phoneNumber: String, bookingHistoryID: List[String] )
  case class Booking(bookingID: String, usernameBook: String, time: String, totalPrice: Double, campBookedListID: List[String])

  case class Camp(campID: String,
                  price: Double,
                  campImgSrc: String,
                  partAddress: String,
                  nearAddress: String,
                  siteDetailsID: String,
                  siteAvailabilityID: String,
                  vehicleDetailsID: String,
                  allowableEquipment: String)

  case class CampSiteDetails(CampSiteDetailsID: String,
                             siteType: String,
                             siteAccessible: String,
                             checkInTime: String,
                             checkOutTime: String,
                             maxNumOfPeople: Int,
                             minNumOfPeople: Int,
                             typeOfUse: String,
                             siteReserveType: String,
                             campFireAllowed: Boolean,
                             capacityRating: String,
                             petAllowed: Boolean,
                             shade: String)

  case class CampAllowableEquipment(allowableEquipmentID: String, items: Map[String, String])
  case class CampSiteAvailability(campSiteAvailabilityID: String, date: String, state: String)

  case class CampVehicleDetails(CampVehicleDetailsID: String,
                                drivewayEntry: String,
                                drivewayLength: Double,
                                drivewaySurface: String,
                                isEquipmentMandatory: String,
                                maxNumOfVehicles: Int,
                                maxVehicleLength: Int,
                                siteLength: Int)

}
