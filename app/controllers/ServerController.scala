package controllers

import play.api._
import play.api.mvc._
import org.bson.types.ObjectId
import play.api.data._
import play.api.data.Forms._
import models.Repository
import models.Server

object ServerController extends Controller {
  val coll = Repository.getCollection("servers")

  //  	"_id" : ObjectId("516d527e0b5eea9fecdd4b69"),
  //	"ip" : "192.168.0.1",
  //	"name" : "First",
  //	"physicalLocation" : "Gdańsk",
  //	"details" : "First server of Kainos office server room."

  val createServerForm: Form[Server] = Form(
    mapping(
      "_id" -> ignored(new ObjectId),
      "ip" -> nonEmptyText,
      "name" -> nonEmptyText,
      "physicalLocation" -> nonEmptyText,
      "details" -> nonEmptyText)(Server.apply)(Server.unapply))

  def index = Action {
    Ok(views.html.servers.index(coll))
  }
  def create = Action {
    Ok(views.html.servers.create())
  }
  //  def details(id: ObjectId) = Action {
  //    
  //    Ok(views.html.servers.details(coll.last))
  //  }
}