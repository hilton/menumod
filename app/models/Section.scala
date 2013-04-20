package models

/**
 * A menu section, such as ‘starters’, ‘cake’ or ‘beer’.
 */
case class Section(title: Option[String], price: Option[String] = None, dishes: List[Dish] = List.empty)

import play.api.libs.functional.syntax._
import play.api.libs.json.Format
import play.api.libs.json.JsPath
import play.api.libs.json.Reads
import play.api.libs.json.Reads._
import play.api.libs.json.Writes

object Section {

  val sectionReads: Reads[Section] = (
    (JsPath \ "title").readNullable[String] and
      (JsPath \ "price").readNullable[String] and
      (JsPath \ "dishes").read( list[Dish](Dish.dishReads) )
    )(Section.apply _)

  val sectionWrites: Writes[Section] = (
    (JsPath \ "title").write[Option[String]] and
      (JsPath \ "price").write[Option[String]] and
      (JsPath \ "dishes").write(Writes.traversableWrites[Dish](Dish.dishWrites))
    )(unlift(Section.unapply))

  implicit val sectionFormat: Format[Section] = Format(sectionReads, sectionWrites)

}
