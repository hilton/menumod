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
case class Menu(uuid: String, title: String = "")

/**
 * Cafe menu data access layer.
 */
object Menu {

  val mongoClient = MongoClient()

  implicit val menuFormat = (
    (JsPath \ "uuid").format[String] and
    (JsPath \ "title").format[String]
    )(Menu.apply, unlift(Menu.unapply))

  /**
   * Creates a new menu.
   */
  def create: Menu = {
    val menu = Menu(UUID.randomUUID().toString  )
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
      Json.parse(JSON.serialize(menu).toString).as[Menu]
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