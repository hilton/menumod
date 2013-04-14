package models

import com.mongodb.casbah.Imports._
import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import com.mongodb.util.JSON
import play.api.Logger

/**
 * A cafe menu.
 */
//case class Menu(uuid: String = UUID.randomUUID().toString, title: String = "", dishes: List[Dish] = List.empty)
case class Menu(uuid: String = UUID.randomUUID().toString, title: String = "", dishes: List[String] = List.empty)

/**
 * A dish or drink on a menu.
 */
case class Dish(name: String, price: String)

/**
 * Cafe menu data access layer.
 */
object Menu {

  val mongoClient = MongoClient()

  import play.api.libs.json.Reads._

  val menuReads: Reads[Menu] = (
    (JsPath \ "uuid").read[String] and
    (JsPath \ "title").read[String] and
//    (JsPath \ "dishes").read( list[Dish](dishReads) )
    (JsPath \ "dishes").read[List[String]]
    )(Menu.apply _)

  val dishReads: Reads[Dish] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "price").read[String]
    )(Dish.apply _)

  import play.api.libs.json.Writes._

  val menuWrites: Writes[Menu] = (
    (JsPath \ "uuid").write[String] and
    (JsPath \ "title").write[String] and
//    (JsPath \ "dishes").write(Writes.traversableWrites[Dish](dishWrites))
    (JsPath \ "dishes").write(Writes.traversableWrites[String])
    )(unlift(Menu.unapply))

  val dishWrites: Writes[Dish] = (
    (JsPath \ "name").write[String] and
    (JsPath \ "price").write[String]
    )(unlift(Dish.unapply))


  implicit val menuFormat: Format[Menu] = Format(menuReads, menuWrites)

  /**
   * Creates a new menu.
   */
  def create: Menu = {
    val menu = Menu()
    val menus = mongoClient("menumod")("menus")
    menus.insert(JSON.parse(Json.toJson(menu).toString).asInstanceOf[DBObject])
    menu
  }

  /**
   * Fetches the menu with the given UUID.
   */
  def find(uuid: String): Option[Menu] = {
    val menus = mongoClient("menumod")("menus")
    val result = menus.findOne(MongoDBObject("uuid" -> uuid))
    result map { menu =>
      val json = JSON.serialize(menu).toString
      Logger.debug("menu = %s" format json)
      Json.parse(json).as[Menu]
    }
  }

  /**
   * Updates an existing menu.
   */
  def update(menu: Menu) {
    Logger.debug("Menu.update(%s)" format menu.uuid)
    val menus = mongoClient("menumod")("menus")
    val dbObject = JSON.parse(Json.toJson(menu).toString).asInstanceOf[DBObject]
    menus.update(MongoDBObject("uuid" -> menu.uuid), dbObject)
  }
}