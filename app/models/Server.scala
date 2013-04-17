package models

import com.mongodb.casbah.Imports._
import play.api.Play

class Server(name: String) {
  val db = MongoClient(Play.current.configuration.getString("mongohost").getOrElse(null),
      Play.current.configuration.getString("mongoport").getOrElse(null).toInt)(Play.current.configuration.getString("mongodbname").getOrElse(null))
  val servers = db.getCollection("servers");
      
//  val id = 
//  val ip
//  val name
//  val physicalLocation
//  val details
//  
  def showAll = {
    //    	  println(db.toString());
    servers.findOne().get(name).toString()
  }
}