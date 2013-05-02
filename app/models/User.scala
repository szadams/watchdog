package models

import org.bson.types.ObjectId
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import play.api.Logger

case class User(id: ObjectId, email: String, name: String, password: String) extends RepositoryObject

object User extends RepositoryAccess[User] {

  override val collectionName = "users"

  override def toModel(dbObject: MongoDBObject) = {
    User(
      dbObject.as[ObjectId]("_id"),
      dbObject.as[String]("email"),
      dbObject.as[String]("name"),
      dbObject.as[String]("password"))
  }

  override def toMongoDBObject(user: User): MongoDBObject = {
    MongoDBObject(
      "_id" -> user.id,
      "email" -> user.email,
      "name" -> user.name,
      "password" -> user.password)
  }

  // -- Parsers

  /**
   * Parse a User from a ResultSet
   */
  //  val simple = {
  //    get[String]("user.email") ~
  //      get[String]("user.name") ~
  //      get[String]("user.password") map {
  //        case email ~ name ~ password => User(email, name, password)
  //      }
  //  }

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
    val auth = mongoDB(collectionName).findOne(MongoDBObject("email" -> email, "password" -> password))
    if (auth.isDefined) {
      Option(toModel(auth.get))
    } else 
    {
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
}