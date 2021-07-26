package CampRestful.User

import Routes.Data._
import spray.json.DefaultJsonProtocol._
import scala.concurrent.Future

case object CalenderHandler {
  case class SiteDetailsForCamp(date: String, state: String)
  implicit val siteDetailsForCampFormat = jsonFormat2(SiteDetailsForCamp)

  def InsertBookingCalenderToDb(booking: Booking ): Future[List[SiteDetails]] = {
    ???
  }

  def GetSiteDetailsForCampPage(campId: String, startDay: String, endDay: String): List[SiteDetails] = {
    ???
  }

  def HandleRawData(siteDetails: List[SiteDetails]): SiteDetailsForCamp = {
    ???
  }
}
