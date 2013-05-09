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
import play.api.i18n.Messages
import play.api.i18n.Lang

object UserController extends Controller with Secured {

  private val createUserForm: Form[User] = Form(
    mapping(
      "_id" -> ignored(new ObjectId),
      "email" -> nonEmptyText.verifying(Messages("views.login.wrongEmail"), email =>
        email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")),
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
      "role" -> nonEmptyText)(User.apply)(User.unapply))

  def list = IsAuthenticated { username =>
    implicit request =>
      User.findByEmail(username).map { user =>
        Ok(views.html.users.list(User.all, user, lang))

      }.getOrElse(Forbidden)
  }

  def create = IsAuthenticated { username =>
    implicit request =>
      User.findByEmail(username).map { user =>
        Ok(
          views.html.users.create(
            createUserForm,
            user, lang))
      }.getOrElse(Forbidden)
  }

  def save = IsAuthenticated { username =>
    implicit request =>
      User.findByEmail(username).map { user =>

        val newCreateUserForm = this.createUserForm.bindFromRequest()

        newCreateUserForm.fold(hasErrors = { form => Redirect(routes.UserController.create()).flashing("error" -> "Something went wrong. It will not be saved.") },
          success = { newUser =>
            User.save(newUser)
            Redirect(routes.ServerController.list()).flashing("success" -> "New user created :)")
          })
      }.getOrElse(Forbidden)
  }

  def view(id: String) = IsAuthenticated { username =>
    implicit request =>
      User.findByEmail(username).map { user =>
        val newObj = User.findById(new ObjectId(id))
        Ok(views.html.users.view(newObj.getOrElse(null), user, lang))
      }.getOrElse(Forbidden)
  }
}