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
}