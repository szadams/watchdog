package models

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import play.api.Logger

case class User(id: ObjectId, email: String, name: String, password: String, role: String) extends RepositoryObject

object User extends RepositoryAccess[User] {

  override val collectionName = "users"

  override def toModel(dbObject: MongoDBObject) = {
    User(
      dbObject.as[ObjectId]("_id"),
      dbObject.as[String]("email"),
      dbObject.as[String]("name"),
      dbObject.as[String]("password"),
      dbObject.as[String]("role"))
  }

  override def toMongoDBObject(user: User): MongoDBObject = {
    MongoDBObject(
      "_id" -> user.id,
      "email" -> user.email,
      "name" -> user.name,
      "password" -> user.password,
      "role" -> user.role)
  }

  // -- Queries

  /**
   * Retrieve a User from email.
   */
  def findByEmail(email: String): Option[User] = {
    mongoDB(collectionName).findOne(MongoDBObject("email" -> email)).map(toModel(_))
  }

  /**
   * Retrieve all users.
   */
  def findAll: Seq[User] = {
    mongoDB(collectionName).find().map(toModel(_)).toList
  }

  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Option[User] = {
    if (email.endsWith("@kainos.com")) {
      val auth = mongoDB(collectionName).findOne(MongoDBObject("email" -> email, "password" -> password))
      if (auth.isDefined)
        Option(toModel(auth.get))
      else
        Option(null)
    } else {
      Option(null)
    }
  }

  /**
   * Create a User.
   */
  def create(user: User): User = {
    val users = mongoDB(collectionName)
    val mongoUser = toMongoDBObject(user)
    users += mongoUser
    user
  }
  
  def isInRole(role: String, user: String): Boolean = {
    val userFromDB = mongoDB(collectionName).findOne(MongoDBObject("email" -> user)) // assuming that username is email
    if(userFromDB.isDefined){
      val userModel = toModel(userFromDB.getOrElse(null))
      userModel.role.equals(role)      
    } else false
  }
}