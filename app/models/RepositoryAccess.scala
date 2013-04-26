package models

import play.api.Play
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

trait RepositoryObject {
  def id: ObjectId
}

trait RepositoryAccess[T <: RepositoryObject] {
  val collectionName: String

  val hostname = Play.current.configuration.getString("mongohost").getOrElse(null)
  val port = Play.current.configuration.getString("mongoport").getOrElse(null).toInt
  val dbname = Play.current.configuration.getString("mongodbname").getOrElse(null)
  val mongoConn = MongoConnection(hostname, port)
  val mongoDB = mongoConn(dbname)

  def toModel(dbObject: MongoDBObject): T
  def toMongoDBObject(repositoryMongoDBObject: T): MongoDBObject

  def save(RepositoryObject: T) = mongoDB(collectionName).insert(toMongoDBObject(RepositoryObject))

  def remove(RepositoryObject: T) = mongoDB(collectionName).remove(toMongoDBObject(RepositoryObject))

  def remove(mongoDBObjectId: ObjectId) = mongoDB(collectionName).remove(MongoDBObject("_id" -> mongoDBObjectId))

  def removeAll = mongoDB(collectionName).remove(MongoDBObject.empty)

  def update(RepositoryObject: T) = mongoDB(collectionName).update(MongoDBObject("_id" -> RepositoryObject.id), toMongoDBObject(RepositoryObject), false, false)

  def findById(id: ObjectId) = mongoDB(collectionName).findOneByID(id).map(toModel(_))

  def all = mongoDB(collectionName).find.map(toModel(_)).toIterable
}