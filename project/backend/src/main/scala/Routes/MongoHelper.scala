package Routes

import CampServer.MongoConfig.MongoController.GetCollection
import com.mongodb.client.MongoCollection
import org.bson.Document

case object MongoHelper {
  val campCollection: MongoCollection[Document] = GetCollection("Camp")
  val userCollection: MongoCollection[Document] = GetCollection("User")
  val bookingCollection: MongoCollection[Document] = GetCollection("Booking")
  val siteDetailsCollection: MongoCollection[Document] = GetCollection("SiteDetails")
  val allowableEquipmentCollection: MongoCollection[Document] = GetCollection("AllowableEquipment")
  val siteAvailabilityCollection: MongoCollection[Document] = GetCollection("SiteAvailability")
  val allowableVehicleAndDrivewayDetailsCollection: MongoCollection[Document] = GetCollection("AllowableVehicleAndDrivewayDetails")

}
