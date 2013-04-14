package models

/**
 * A dish or drink on a menu.
 */
case class Dish(name: String, price: Option[String] = None)

import play.api.libs.functional.syntax._
import play.api.libs.json.Format
import play.api.libs.json.JsPath
import play.api.libs.json.Reads
import play.api.libs.json.Reads._
import play.api.libs.json.Writes

object Dish {

  val dishReads: Reads[Dish] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "price").readNullable[String]
    )(Dish.apply _)

  val dishWrites: Writes[Dish] = (
    (JsPath \ "name").write[String] and
      (JsPath \ "price").write[Option[String]]
    )(unlift(Dish.unapply))

  implicit val dishFormat: Format[Dish] = Format(dishReads, dishWrites)

}
