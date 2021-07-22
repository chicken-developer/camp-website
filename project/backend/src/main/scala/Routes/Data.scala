package Routes

import spray.json.DefaultJsonProtocol._

case object Data {
  implicit val objectIdFormat = jsonFormat1(ObjectId)
  implicit val userFormat = jsonFormat9(User)
  implicit val bookingFormat = jsonFormat6(Booking)
  implicit val campFormat = jsonFormat10(Camp)
  implicit val campSiteDetailsFormat = jsonFormat14(CampSiteDetails)
  implicit val campAllowableEquipmentFormat = jsonFormat3(CampAllowableEquipment)
  implicit val campSiteAvailabilityFormat = jsonFormat4(CampSiteAvailability)
  implicit val campVehicleDetailsFormat = jsonFormat9(CampVehicleDetails)
  implicit val messageFormat3 = jsonFormat3(Message)
  case class Message(message: String, status: Int, data: String)
  case class ObjectId(_objId: String)

  case class User(_id: ObjectId, username: String, typeOfUser: String, firstName: String, lastName: String, password: String, email: String, phoneNumber: String, bookingHistoryId: List[String])

  val templateUser = User(ObjectId("template"), "username", "typeOfUser", "firstName", "lastName", "password", "email", "phoneNumber", List("b_345345", "b_123123"))

  case class Booking(_id: ObjectId, bookingId: String, usernameBooked: String, time: String, totalPrice: Double, campBookedId: List[String])

  val templateBooking = Booking(ObjectId("template"), "bookingID", "usernameBook", "time", 324.2, List("sdasd1", "wdasdasdasd"))

  case class Camp(_id: ObjectId, campId: String,
                  price: Double,
                  campImgSrc: String,
                  partAddress: String,
                  nearAddress: String,
                  siteDetailsId: String,
                  siteAvailabilityId: String,
                  vehicleDetailsId: String,
                  allowableEquipmentListId: String)

  val templateCamp = Camp(ObjectId("template"), "123123123", 20.2, "campImgSrc", "partAddress", "nearAddress", "siteDetailsID", "siteAvailabilityID", "vehicleDetailsID", "allowableEquipment")

  case class CampSiteDetails(_id: ObjectId,
                             campSiteDetailsId: String,
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

  val templateCampSiteDetails = CampSiteDetails(ObjectId("template"), "campSiteDetailsId", "siteType",
    "siteAccessible",
    "checkInTime",
    "checkOutTime", 22, 10,
    "typeOfUse",
    "siteReserveType", true,
    "capacityRating", true,
    "shade")

  case class CampAllowableEquipment(_id: ObjectId, allowableEquipmentId: String, items: Map[String, String])

  val templateCampAllowableEquipment = CampAllowableEquipment(ObjectId("template"), "ae_123123", Map("123" -> "123123", "123" -> "123123"))

  case class CampSiteAvailability(_id: ObjectId, campSiteAvailabilityId: String, date: String, state: String)

  val templateCampSiteAvailability = CampSiteAvailability(ObjectId("template"), "sa_123123", "2020_20_21", "test")

  case class CampVehicleDetails(_id: ObjectId,
                                campVehicleDetailsId: String,
                                drivewayEntry: String,
                                drivewayLength: Double,
                                drivewaySurface: String,
                                isEquipmentMandatory: String,
                                maxNumOfVehicles: Int,
                                maxVehicleLength: Int,
                                siteLength: Int)

  val templateCampVehicleDetails = CampVehicleDetails(ObjectId("template"), "CampVehicleDetailsId", "drivewayEntry", 90.2, "drivewaySurface", "isEquipmentMandatory", 12, 120, 10)

}
