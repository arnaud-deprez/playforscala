package controllers

import play.api.data.validation.ValidationError
import play.api.libs.json.{JsString, JsPath, Writes}
import play.api.libs.functional.syntax._

/**
 * Created by arnaud.deprez on 18/08/15.
 */
package object utils {
  implicit val JsPathWrites =
    Writes[JsPath](p => JsString(p.toString))
  implicit val ValidationErrorWrites =
    Writes[ValidationError](e => JsString(e.message))
  implicit val jsonValidateErrorWrites = (
    (JsPath \ "path").write[JsPath] and
      (JsPath \ "errors").write[Seq[ValidationError]]
      tupled
    )

}
