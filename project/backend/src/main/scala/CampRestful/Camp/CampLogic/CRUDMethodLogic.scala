package CampRestful.Camp.CampLogic

import Routes.Data.{CampData, templateCampData}

case object CRUDMethodLogic {
  def InsertNewCampToDatabase(campData: CampData): CampData =
  {
    templateCampData
  }
  def UpdateCampFromDatabase(newData: CampData, campId: String): CampData = {
    templateCampData
  }
}
