package Routes

import CampServer.MongoConfig.MongoController
import Core.Data.Camp
import org.bson.Document
import org.mongodb.scala.MongoCollection
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.enrichAny

import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.collection.mutable.ListBuffer


/*

 */
import MongoController._

case object RouteLogic {
  def GetCamp(): List[Camp] = {
    println("LOG_CAMP")
    val campCollection = GetCollection("Camp")
    val allCamps = campCollection.find()
      .map { camp =>
         camp.toString.toJson.prettyPrint
      }
    val demoallCamps = List[Camp]()
    demoallCamps
  }
}
