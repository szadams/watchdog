package controllers

import play.api._
import play.api.mvc._

object Login extends Controller {
  def login = Action {
    Ok(views.html.login())
  }
}