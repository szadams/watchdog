package models

import com.mongodb.casbah.Imports._
import play.api.Play

class Server(name: String) {
  val hostname = Play.current.configuration.getString("mongohost").getOrElse(null)
  val port = Play.current.configuration.getString("mongoport").getOrElse(null).toInt
  val dbname = Play.current.configuration.getString("mongodbname").getOrElse(null)
  val mongoConn = MongoConnection(hostname, port)
  val mongoDB = mongoConn(dbname)

  val collection = mongoDB("servers")

  //  val id = 
  //  val ip
  //  val name
  //  val physicalLocation
  //  val details
  //  
  def showAll = { // only descriptions for now
    var str: String = ""
    collection.foreach(s => str += s.get("details") + " ")
    //    for (i <- 1 to collection.size) {
    //      str += collection.//get(i).toString()
    //      str += " "
    //    }
    str
  }
}