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

  private val createServerForm: Form[Server] = Form(
    mapping(
      "_id" -> ignored(new ObjectId), // TODO: to check it, this may cause problems in mongo
      "ip" -> nonEmptyText,
      "name" -> nonEmptyText,
      "physicalLocation" -> nonEmptyText,
      "details" -> nonEmptyText)(Server.apply)(Server.unapply))

  def list = Action {
    Ok(views.html.servers.list(Server.all))
    // TODO: routes.ServerController.list
  }
  def create = Action {
    Ok(views.html.servers.create(createServerForm))
  }
  def save = Action { implicit request =>
    val newCreateServerForm = this.createServerForm.bindFromRequest()

    newCreateServerForm.fold(hasErrors = { form => Redirect(routes.ServerController.create()) },
      success = { newServer =>
        Server.save(newServer)
        Ok(views.html.servers.list(Server.all))
      })
  }
  def view(id: String) = Action {
    val newObj = Server.findById(new ObjectId(id))
    Ok(views.html.servers.view(newObj.getOrElse(null)))
  }
}