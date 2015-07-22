package controllers

import javax.inject.Inject

import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc.{Action, Controller}
import models.Product

/**
  * Created by arnaud.deprez on 22/07/15.
 */
class Products @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def list = Action { implicit request =>
    val products = Product.findAll
    Ok(views.html.products.list(products))
  }
}
