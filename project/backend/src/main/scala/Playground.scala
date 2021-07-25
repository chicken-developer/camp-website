import spray.json.enrichAny

import java.time.LocalDate
import java.util.stream.Collectors

object Playground extends App{
 import Routes.Data._
  //Using for convert to json to insert into database
//  println(templateUser.toJson.prettyPrint)
//  println(templateBooking.toJson.prettyPrint)
//  println(templateCamp.toJson.prettyPrint)
//  println(templateSiteAvailability.toJson.prettyPrint)
//  println(templateSiteDetails.toJson.prettyPrint)
//  println(templateAllowableEquipment.toJson.prettyPrint)
//  println(templateAllowableVehicleAndDrivewayDetails.toJson.prettyPrint)

 def dates(fromDate: LocalDate): Stream[LocalDate] = {
  fromDate #:: dates(fromDate plusDays 1 )
 }
 val date = dates(LocalDate.of(2020, 2, 25)).takeWhile(_.isBefore(LocalDate.of(2020, 3, 2))).toList
 println(date)
}
