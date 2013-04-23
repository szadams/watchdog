package models

import org.bson.types.ObjectId

case class Server(id: ObjectId, ip: String, name: String, physicalLocation: String, details: String)

//class Server{
// val id: ObjectId 
// val ip: String
// val name: String
// val physicalLocation: String
// val details: String
//}


//  val hostname = Play.current.configuration.getString("mongohost").getOrElse(null)
//  val port = Play.current.configuration.getString("mongoport").getOrElse(null).toInt
//  val dbname = Play.current.configuration.getString("mongodbname").getOrElse(null)
//  val mongoConn = MongoConnection(hostname, port)
//  val mongoDB = mongoConn(dbname)

//  val collection = mongoDB("servers")

  //  val id = 
  //  val ip
  //  val name
  //  val physicalLocation
  //  val details
  //  
//  def showAll = { // only descriptions for now
//    var str: String = ""
//    collection.foreach(s => str += s.get("details") + " ")
//    str
//  }
//  def getCollection(name: String) = {
//    mongoDB(name)
//  }
