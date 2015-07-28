package controllers

import javax.inject.Inject

import models.Product
import play.api.i18n.{Messages, I18nSupport, MessagesApi}
import play.api.mvc.{Flash, Action, Controller}
import play.api.data._
import play.api.data.Forms._

/**
 * Created by arnaud.deprez on 22/07/15.
 */
class Products @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  var products: Set[Product] = Set()

  private val productForm: Form[Product] = Form(
    mapping(
      "ean" -> longNumber.verifying("validation.ean.duplicate", Product.findByEan(_).isEmpty),
      "name" -> nonEmptyText,
      "description" -> nonEmptyText
    )(Product.apply)(Product.unapply)
  )

  def list = Action { implicit request =>
    val products = Product.findAll
    Ok(views.html.products.list(products))
  }

  def show(ean: Long) = Action { implicit request =>
    Product.findByEan(ean).map { product =>
      Ok(views.html.products.details(product))
    }.getOrElse(NotFound)
  }

  def save = Action { implicit request =>
    val newProductForm: Form[Product] = productForm.bindFromRequest()

    newProductForm.fold(
      hasErrors = { form =>
        Redirect(routes.Products.newProduct())
          .flashing(Flash(form.data) + ("error" -> Messages("validation.errors")))
      },
      success = { newProduct =>
        Product.add(newProduct)
        val message = Messages("products.new.success", newProduct.name)
        Redirect(routes.Products.show(newProduct.ean)).
          flashing("success" -> message)
      })
  }

  def newProduct = Action { implicit request =>
    val form = if (request.flash.get("error").isDefined)
      productForm.bind(request.flash.data)
    else
      productForm
    Ok(views.html.products.edit(form))
  }
}
