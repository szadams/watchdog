package models

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject

case class Server(id: ObjectId, ip: String, name: String, physicalLocation: String, details: String) extends RepositoryObject

object Server extends RepositoryAccess[Server] {

  override val collectionName = "servers"

  override def toModel(dbObject: MongoDBObject) = {
    Server(
      dbObject.as[ObjectId]("_id"),
      dbObject.as[String]("ip"),
      dbObject.as[String]("name"),
      dbObject.as[String]("physicalLocation"),
      dbObject.as[String]("details"))
  }

  override def toMongoDBObject(server: Server): MongoDBObject = {
    MongoDBObject(
      "_id" -> server.id,
      "ip" -> server.ip,
      "name" -> server.name,
      "physicalLocation" -> server.physicalLocation,
      "details" -> server.details)
  }

  def findByName(name: String) = {
    mongoDB(collectionName).findOne(MongoDBObject("name" -> name)).map(toModel(_))
  }
}