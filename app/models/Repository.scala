package models

import play.api.Play
import com.mongodb.casbah.Imports._

object Repository {
  val hostname = Play.current.configuration.getString("mongohost").getOrElse(null)
  val port = Play.current.configuration.getString("mongoport").getOrElse(null).toInt
  val dbname = Play.current.configuration.getString("mongodbname").getOrElse(null)
  val mongoConn = MongoConnection(hostname, port)
  val mongoDB = mongoConn(dbname)

  def getCollection(name: String) = {
    mongoDB(name)
  }

  def addServer(s: Server) = {
    val newServer = MongoDBObject(
      "_id" -> s.id,
      "ip" -> s.ip,
      "name" -> s.name,
      "physicalLocation" -> s.physicalLocation,
      "details" -> s.details)
    mongoDB("servers") += newServer
  }

  def removeServer(id: ObjectId) = {
    mongoDB("servers").remove(MongoDBObject("_id" -> id))
  }

  def removeServer(id: String) = {
    val tempId = new ObjectId(id)
    mongoDB("servers").remove(MongoDBObject("_id" -> tempId))
  }

  def removeAllServers() = {
    mongoDB("servers").remove(MongoDBObject.empty)
  }

//  def editServer(id: ObjectId, element: String, newValue: String) = {
//    val obj = mongoDB("servers").findOne(MongoDBObject("_id" -> id))
//    mongoDB("servers").update(obj, $set("name" -> "Third"))
//  }
}