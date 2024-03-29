package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok("Hello World!")
  }

  def hello(name: String) = Action {
    Ok(views.html.hello(name))
  }

}
