package Core

import akka.actor.Status.Success
import org.bson.Document
import spray.json.DefaultJsonProtocol._
import spray.json.enrichAny

import scala.util.Failure

case object Data {
  implicit val objectIdFormat = jsonFormat1(ObjectId)
  implicit val userFormat = jsonFormat9(User)
  implicit val bookingFormat = jsonFormat6(Booking)
  implicit val campFormat = jsonFormat10(Camp)
  implicit val campSiteDetailsFormat = jsonFormat14(CampSiteDetails)
  implicit val campAllowableEquipmentFormat = jsonFormat3(CampAllowableEquipment)
  implicit val campSiteAvailabilityFormat = jsonFormat4(CampSiteAvailability)
  implicit val campVehicleDetailsFormat = jsonFormat9(CampVehicleDetails)

  case class ObjectId(_objId: String)
  case class User(_id: ObjectId, username: String, typeOfUser: String, firstName: String, lastName: String, password: String, email: String, phoneNumber: String, bookingHistoryID: List[String])
  val templateUser = User(ObjectId("12312312"), "username", "typeOfUser", "firstName", "lastName", "password", "email" , "phoneNumber",List("b_345345", "b_123123"))
  case class Booking(_id: ObjectId, bookingID: String, usernameBook: String, time: String, totalPrice: Double, campBookedListID: List[String])
  val templateBooking = Booking(ObjectId("12312312"),"bookingID", "usernameBook", "time", 324.2, List("sdasd1","wdasdasdasd"))
  case class Camp(_id: ObjectId, campId: String,
                  price: Double,
                  campImgSrc: String,
                  partAddress: String,
                  nearAddress: String,
                  siteDetailsId: String,
                  siteAvailabilityId: String,
                  vehicleDetailsId: String,
                  allowableEquipmentListId: String)

  val templateCamp = Camp(ObjectId("12312312"),"123123123", 20.2, "campImgSrc", "partAddress", "nearAddress", "siteDetailsID" , "siteAvailabilityID","vehicleDetailsID" , "allowableEquipment")

  case class CampSiteDetails(_id: ObjectId,
                             campSiteDetailsID: String,
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

  case class CampAllowableEquipment(_id: ObjectId,
                                    allowableEquipmentID: String, items: Map[String, String])

  case class CampSiteAvailability(_id: ObjectId,campSiteAvailabilityID: String, date: String, state: String)

  case class CampVehicleDetails(_id: ObjectId,
                                CampVehicleDetailsID: String,
                                drivewayEntry: String,
                                drivewayLength: Double,
                                drivewaySurface: String,
                                isEquipmentMandatory: String,
                                maxNumOfVehicles: Int,
                                maxVehicleLength: Int,
                                siteLength: Int)

}
