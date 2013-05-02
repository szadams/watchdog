package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.User

object Login extends Controller {

  // Authentication

//  val loginForm = Form(
//    tuple("email" -> nonEmptyText,
//      "password" -> nonEmptyText) verifying ("Invalid email or password", result => result match {
//        case (email, password) => User.authenticate(email, password).isDefined
//      }))

  val loginForm = Form(
    tuple("email" -> text,
      "password" -> text) verifying ("Invalid email or password", result => result match {
        case (email, password) => User.authenticate(email, password).isDefined
      }))
  
  // Login page

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  // Handle login form submission

  def authenticate = Action { implicit request =>
    Logger.info("Action: authenticate")
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.ServerController.list).withSession("email" -> user._1))
  }

  // Logout and clean the session

  def logout = Action {
    Redirect(routes.Login.login).withNewSession.flashing("success" -> "You've been logged out")
  }
}

/**
 * Provide security features
 */
trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Login.login)

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

  /**
   * Check if the connected user is a member of this project.
   */
  //  def IsMemberOf(project: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user =>
  //    request =>
  //      if (Project.isMember(project, user)) {
  //        f(user)(request)
  //      } else {
  //        Results.Forbidden
  //      }
  //  }

  /**
   * Check if the connected user is a owner of this task.
   */
  //  def IsOwnerOf(task: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user =>
  //    request =>
  //      if (Task.isOwner(task, user)) {
  //        f(user)(request)
  //      } else {
  //        Results.Forbidden
  //      }
  //  }

}