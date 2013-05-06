package controllers

import play.api._
import play.api.mvc._
import org.bson.types.ObjectId
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import models.RepositoryAccess
import models._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.DBObject
import play.data.validation.Validation
import play.i18n.Messages

object ServerController extends Controller with Secured {

  private val createServerForm: Form[Server] = Form(
    mapping(
      "_id" -> ignored(new ObjectId), // TODO: to check it, this may cause problems in mongo
      "ip" -> nonEmptyText.verifying(Messages.get("views.server.create.ipInvalid"), ip =>
        ip.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")),
      "name" -> nonEmptyText,
      "physicalLocation" -> nonEmptyText.verifying(Messages.get("views.server.create.locationInvalid"), pl => pl.matches("[a-zA-Z0-9]+$")),
      "details" -> text)(Server.apply)(Server.unapply))

  def list = IsAuthenticated { username =>
    _ =>
      User.findByEmail(username).map { user =>
        Ok(views.html.servers.list(Server.all, user))

      }.getOrElse(Forbidden)
  }

  def create = IsAuthenticated { username =>
    _ =>
      User.findByEmail(username).map { user =>
        Ok(
          views.html.servers.create(
            createServerForm,
            user))
      }.getOrElse(Forbidden)
  }

  def save = IsAuthenticated { username =>
    implicit request =>
      User.findByEmail(username).map { user =>

        val newCreateServerForm = this.createServerForm.bindFromRequest()

        newCreateServerForm.fold(hasErrors = { form => Redirect(routes.ServerController.create()) },
          success = { newServer =>
            Server.save(newServer)
            Ok(views.html.servers.list(Server.all, user))
          })
      }.getOrElse(Forbidden)
  }

  def view(id: String) = IsAuthenticated { username =>
    _ =>
      User.findByEmail(username).map { user =>
        val newObj = Server.findById(new ObjectId(id))
        Ok(views.html.servers.view(newObj.getOrElse(null), user))
      }.getOrElse(Forbidden)
  }
}