package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  val Logger = org.slf4j.LoggerFactory.getLogger("application")

  def welcome = Action { implicit request =>
    Ok(views.html.play20.welcome("Default Welcome Page"))
  }

  def hello(name: String) = Action { implicit request =>
    Ok(views.html.hello(name))
  }
}
