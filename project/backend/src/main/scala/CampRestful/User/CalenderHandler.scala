package CampRestful.User

import CampRestful.Camp.CampLogic.CampConverter
import Routes.Data._
import Routes.MongoHelper._
import spray.json.DefaultJsonProtocol._

import java.time.LocalDate
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case object CalenderHandler {
  case class SiteDetailsForCamp(date: String, state: String)
  implicit val siteDetailsForCampFormat = jsonFormat2(SiteDetailsForCamp)
  def GetAllBookingDate(): List[SiteAvailability] =
  {
    val allSiteAvailability = siteAvailabilityCollection.find()
      .map { siteAvailability =>
        CampConverter.ConvertToSiteAvailability(siteAvailability.toString.replaceAll("Document", "SiteAvailability"))
      }.toList
    allSiteAvailability
  }

  def InsertBookingCalenderToDb(booking: Booking ): Future[Booking] = {
    //TODO: Insert booking to database and siteDetails update
    Future(booking)
  }



  def HandleRawData(campId: String, startDay: String, endDay: String): List[SiteDetailsForCamp] = {
    def dates(fromDate: LocalDate): Stream[LocalDate] = {
      fromDate #:: dates(fromDate plusDays 1 )
    }
    val startYear = 2021
    val startMonth = 7
    val startDay = 1
    val endYear = 2021
    val endMonth = 9
    val endDay = 1
    val allData = GetAllBookingDate()
    val date = dates(LocalDate.of(startYear, startMonth, startDay)).takeWhile(_.isBefore(LocalDate.of(endYear, endMonth, endDay))).toList
    date.map{ d =>
       val result = allData.findLast(_.date == d.toString)
       val siteAvailability = result match {
          case Some(value) => value
          case None => templateSiteAvailability
        }
      val finalR = SiteDetailsForCamp(d.toString, siteAvailability.state)
      finalR
    }
  }



}
