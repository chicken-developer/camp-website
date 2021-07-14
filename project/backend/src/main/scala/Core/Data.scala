package Core

import akka.actor.Status.Success
import org.bson.Document
import spray.json.DefaultJsonProtocol._
import spray.json.enrichAny

import scala.util.Failure

case object Data {
  implicit val userFormat = jsonFormat8(User)
  implicit val bookingFormat = jsonFormat5(Booking)
  implicit val campFormat = jsonFormat10(Camp)
  implicit val campSiteDetailsFormat = jsonFormat13(CampSiteDetails)
  implicit val campAllowableEquipmentFormat = jsonFormat2(CampAllowableEquipment)
  implicit val campSiteAvailabilityFormat = jsonFormat3(CampSiteAvailability)
  implicit val campVehicleDetailsFormat = jsonFormat8(CampVehicleDetails)

  case class User(username: String, typeOfUser: String, firstName: String, lastName: String, password: String, email: String, phoneNumber: String, bookingHistoryID: List[String])
  val templateUser = User("username", "typeOfUser", "firstName", "lastName", "password", "email" , "phoneNumber",List("b_345345", "b_123123"))
  case class Booking(bookingID: String, usernameBook: String, time: String, totalPrice: Double, campBookedListID: List[String])
  val templateBooking = Booking("bookingID", "usernameBook", "time", 324.2, List("sdasd1","wdasdasdasd"))
  case class Camp(_id: String, campId: String,
                  price: Double,
                  campImgSrc: String,
                  partAddress: String,
                  nearAddress: String,
                  siteDetailsId: String,
                  siteAvailabilityId: String,
                  vehicleDetailsId: String,
                  allowableEquipmentListId: String)

  val templateCamp = Camp("123123123","campID", 20.2, "campImgSrc", "partAddress", "nearAddress", "siteDetailsID" , "siteAvailabilityID","vehicleDetailsID" , "allowableEquipment")

  case class CampSiteDetails(campSiteDetailsID: String,
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
