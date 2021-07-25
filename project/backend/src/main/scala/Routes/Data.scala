package Routes

import spray.json.DefaultJsonProtocol._
import spray.json.{JsValue, enrichAny, jsonReader}

case object Data {
  implicit val userFormat = jsonFormat9(User)
  implicit val bookingFormat = jsonFormat6(Booking)
  implicit val campFormat = jsonFormat11(Camp)
  implicit val siteAvailabilityFormat = jsonFormat3(SiteAvailability)
  implicit val siteDetailsFormat = jsonFormat13(SiteDetails)
  implicit val allowableEquipmentFormat = jsonFormat2(AllowableEquipment)
  implicit val allowableVehicleAndDrivewayDetailsFormat = jsonFormat8(AllowableVehicleAndDrivewayDetails)

case class User(_id: String, username: String, typeOfUser: String, firstName: String, lastName: String, password: String, email: String, phoneNumber: String, bookingHistoryId: List[String])

  val templateUser = User("user_template", "test", "normal", "Test", "Bro", "test123", "test@gmail.com", "0123 421123", List("b_345345", "b_123123"))

  case class Booking(_id: String, usernameBooked: String, timeStart: String, timeEnd: String, totalPrice: Double, campBookedId: List[String])

  val templateBooking = Booking("b_123123", "test", "2021:07:24","2021:07:26", 324.2, List("c_123123", "c_456456", "c_789789"))

  case class Camp(_id: String,
                  campName: String,
                  price: Double,
                  campImgSrc: List[String],
                  partAddress: String,
                  nearAddress: String,
                  campLocationAddress: String,
                  siteAvailabilityId: String,
                  siteDetailsId: String,
                  allowableEquipmentListId: String,
                  allowableVehicleAndDrivewayDetailsId: String
                  )

  val templateCamp = Camp("c_123123","Site: A004, Loop: A", 20.2, List("src/pic/01.png","src/pic/02.png"),
            " Stanislaus National Forest", " Pinecrest, California","Pinecrest", "sa_123123",
              "sd_123123", "ae_123123", "av_123123")

  case class SiteAvailability(_id: String,date: String, state: String)
  val templateSiteAvailability = SiteAvailability("sa_123123", "2021:07:24", "R")

  case class SiteDetails(_id: String,
                         siteType: String,
                         siteAccessible: String,
                         checkInTime: String,
                         checkOutTime: String,
                         maxNumOfPeople: Int,
                         minNumOfPeople: Int,
                         typeOfUse: String,
                         siteReserveType: String,
                         capacityRating: String,
                         campFireAllowed: String,
                         petAllowed: String,
                         shade: String)

  val templateSiteDetails = SiteDetails("sd_123123", "Standard Nonelectric",
    "No",
    "2:00 PM",
    "11:00 AM", 6, 1,
    "Over night",
    "Site-Specific", "Single",
    "Yes", "Yes", "Yes")

  case class AllowableEquipment(_id: String, items: Map[String, String])

  val templateAllowableEquipment = AllowableEquipment("ae_123123", Map("Tent" -> "Yes", "RV" -> "max .30ft","Trailer" ->"max .30ft"))

  case class AllowableVehicleAndDrivewayDetails(_id: String,
                                drivewayEntry: String,
                                drivewayLength: Double,
                                drivewaySurface: String,
                                isEquipmentMandatory: String,
                                maxNumOfVehicles: Int,
                                maxVehicleLength: Int,
                                siteLength: Int)

  val templateAllowableVehicleAndDrivewayDetails = AllowableVehicleAndDrivewayDetails("av_123123", "Back-in", 42.0, "Paved", "Yes", 2, 30, 42)
  case class Message(message: String, status: Int, data: JsValue)
  implicit val messageFormat3 = jsonFormat3(Message)

  case class CampForHomePage(_id: String, name: String, mainImgSrc: String, allImgSrc: List[String], address: String,sd_typeOfUse: String,sd_maxNumOfPeople: Int, vd_maxNumOfVehicles: Int,vd_maxVehicleLengthForVehicle: Int,rvMax: Int, tenMax: Int, price: Double)
  implicit val campForHomeFormat = jsonFormat12(CampForHomePage)


  val templateCampForHomePage =  CampForHomePage("c_template","Site: A004, Loop: A",
    "src/pic/123.png", List("src/pic/123.png", "src/pic/456.png"), " Stanislaus National Forest","Overnight",
    6, 2,30,30, 30, 21.14)
  case class CampData(_id: String,
                      campName: String,
                      price: Double,
                      campImgSrc: List[String],
                      partAddress: String,
                      nearAddress: String,
                      campLocationAddress: String,
                      siteAvailability: JsValue,
                      siteDetails: JsValue,
                      allowableEquipmentList: JsValue,
                      allowableVehicleAndDrivewayDetails: JsValue
                     )

  implicit val campDataFormat = jsonFormat11(CampData)
  val templateCampData = CampData("c_123123","Site: A004, Loop: A", 20.2, List("src/pic/01.png","src/pic/02.png"),
    " Stanislaus National Forest", " Pinecrest, California","Pinecrest", templateSiteAvailability.toJson,
    templateSiteDetails.toJson, templateAllowableEquipment.toJson, templateAllowableVehicleAndDrivewayDetails.toJson)


}
