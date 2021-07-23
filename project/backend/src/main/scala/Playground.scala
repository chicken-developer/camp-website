import spray.json.enrichAny

object Playground extends App{
 import Routes.Data._
  //Using for convert to json to insert into database
  println(templateUser.toJson.prettyPrint)
  println(templateBooking.toJson.prettyPrint)
  println(templateCamp.toJson.prettyPrint)
  println(templateSiteAvailability.toJson.prettyPrint)
  println(templateSiteDetails.toJson.prettyPrint)
  println(templateCampAllowableEquipment.toJson.prettyPrint)
  println(templateAllowableVehicleAndDrivewayDetails.toJson.prettyPrint)

}
