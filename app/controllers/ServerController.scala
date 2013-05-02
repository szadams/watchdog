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

object ServerController extends Controller with Secured {

  private val createServerForm: Form[Server] = Form(
    mapping(
      "_id" -> ignored(new ObjectId), // TODO: to check it, this may cause problems in mongo
      "ip" -> nonEmptyText,
      "name" -> nonEmptyText,
      "physicalLocation" -> nonEmptyText,
      "details" -> nonEmptyText)(Server.apply)(Server.unapply))

  def list = IsAuthenticated { username => _ =>
    Logger.info("I'm in list action")
    User.findByEmail(username).map { user =>
      Logger.info("Right before redir to list view")
      Ok(views.html.servers.list(Server.all, user))
      
    }.getOrElse(Forbidden)    
  }
    
  def create = IsAuthenticated { username => _ =>
      User.findByEmail(username).map { user =>
        Ok(
          views.html.servers.create(
              createServerForm,
              user
          )
        )
      }.getOrElse(Forbidden)
  }

  def save = IsAuthenticated { username => implicit request =>
      User.findByEmail(username).map { user =>

        val newCreateServerForm = this.createServerForm.bindFromRequest()

        newCreateServerForm.fold(hasErrors = { form => Redirect(routes.ServerController.create()) },
          success = { newServer =>
            Server.save(newServer)
            Ok(views.html.servers.list(Server.all, user))
          })
      }.getOrElse(Forbidden)
  }
    
  def view(id: String) = IsAuthenticated { username => _ =>
    User.findByEmail(username).map { user =>
      val newObj = Server.findById(new ObjectId(id))
      Ok(views.html.servers.view(newObj.getOrElse(null), user))
    }.getOrElse(Forbidden)
  }
}