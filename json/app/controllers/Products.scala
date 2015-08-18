package controllers

import models.{Company, Contact, Product}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.mvc.{Action, Controller}
import controllers.utils._

/**
 * Created by arnaud.deprez on 16/08/15.
 */
class Products extends Controller {

  //region json converter
  implicit object ProductWrites extends Writes[Product] {
    override def writes(p: Product): JsValue = Json.obj(
      "ean" -> Json.toJson(p.ean),
      "name" -> Json.toJson(p.name),
      "description" -> Json.toJson(p.description)
    )
  }

  implicit val companyReads: Reads[Company] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "contact_details").read((
        (JsPath \ "email").readNullable[String](email) and
          (JsPath \ "fax").readNullable[String](minLength[String](10)) and
          (JsPath \ "phone").readNullable[String](minLength[String](10))
        )(Contact.apply _))
    )(Company.apply _)

  implicit val productReads: Reads[Product] = (
    (JsPath \ "ean").read[Long] and
      (JsPath \ "name").read[String](minLength[String](5)) and
      (JsPath \ "description").readNullable[String] and
      (JsPath \ "pieces").readNullable[Int] and
      (JsPath \ "manufacturer").read[Company] and
      (JsPath \ "tags").read[List[String]] and
      (JsPath \ "active").read[Boolean]
    )(Product.apply _)

  //or
  /*implicit val productFormat: Format[Product] = (
    (JsPath \ "ean").format[Long] and
      (JsPath \ "name").format[String](minLength[String](5)) and
      (JsPath \ "description").formatNullable[String]
    )(Product.apply, unlift(Product.unapply))*/

  // or simply
  //  implicit val productFormat = Json.format[Product] // can do the same with Reads & Writes but it needs to find a formatter for each fields
  //endregion

  def list = Action {
    val productCodes = Product.findAll.map(_.ean)
    Ok(Json.toJson(productCodes))
  }

  def details(ean: Long) = Action {
    Product.findByEan(ean).map { product =>
      Ok(Json.toJson(product))
    }.getOrElse(NotFound)
  }

  def update(ean: Long) = Action(parse.json) { implicit request =>
    val json = request.body
    json.validate[Product].
      fold(
        invalid = {
          errors => BadRequest(JsError.toJson(errors))
        },
        valid = { product =>
          Product.update(product).fold(
            BadRequest(_),
            _ => Ok("Saved")
          )
        })
  }
}
