package controllers

import play.api._
import play.api.mvc._
import org.bson.types.ObjectId
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import models.RepositoryAccess
import models.Server
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.DBObject

object ServerController extends Controller {
  //  val coll = Repository.getCollection("servers")

  private val createServerForm: Form[Server] = Form(
    mapping(
      "_id" -> ignored(new ObjectId),
      "ip" -> nonEmptyText,
      "name" -> nonEmptyText,
      "physicalLocation" -> nonEmptyText,
      "details" -> nonEmptyText)(Server.apply)(Server.unapply))

  def index = Action {
//    val coll = Server.getCollection("servers")

    Ok(views.html.servers.index(Server.all))
    // TODO: routes.ServerController.list
  }
  def create = Action {
    Ok(views.html.servers.create(createServerForm))
  }
  def save = Action { implicit request =>
    val newCreateServerForm = this.createServerForm.bindFromRequest()

    //    val serv = MongoDBObject(
    //      "ip" -> nonEmptyText,
    //      "name" -> nonEmptyText,
    //      "physicalLocation" -> nonEmptyText,
    //      "details" -> nonEmptyText
    //    )

    newCreateServerForm.fold(hasErrors = { form => Redirect(routes.ServerController.create()) },
      success = { newServer =>

        Server.save(newServer)
//        val coll = Repository.getCollection("servers")
        Ok(views.html.servers.index(Server.all))
      })
  }
  //  def details(id: String) = Action {
  //    val newId = new ObjectId(id)
  //    val newObj = Repository.getServer(id)//.getOrElse(new MongoDBObject)
  //    Ok(views.html.servers.details(newObj)) //asInstanceOf[com.mongodb.casbah.Imports.DBObject]
  //  }
}