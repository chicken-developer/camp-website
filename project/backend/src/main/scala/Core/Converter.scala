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
}
