package controllers

import play.api._
import play.api.mvc._
import models.Server

object Application extends Controller {
  val s: Server = new Server("First")
  def index = Action {
    Ok(views.html.index("Some message", s.getCollection("servers")))
  }
}