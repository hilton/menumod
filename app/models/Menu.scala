package models

import com.mongodb.casbah.Imports._
import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._
import com.mongodb.util.JSON
import play.api.{Play, Logger}
import java.io.File
import play.api.libs.Files

/**
 * A cafe menu.
 */
case class Menu(uuid: String = UUID.randomUUID().toString, title: String = "", dishes: List[Dish] = List.empty)

/**
 * Cafe menu data access layer.
 */
object Menu {

  val mongoClient = MongoClient()

  import play.api.libs.json.Reads._

  val menuReads: Reads[Menu] = (
    (JsPath \ "uuid").read[String] and
      (JsPath \ "title").read[String] and
      (JsPath \ "dishes").read( list[Dish](Dish.dishReads) )
    )(Menu.apply _)

  import play.api.libs.json.Writes._

  val menuWrites: Writes[Menu] = (
    (JsPath \ "uuid").write[String] and
      (JsPath \ "title").write[String] and
      (JsPath \ "dishes").write(Writes.traversableWrites[Dish](Dish.dishWrites))
    )(unlift(Menu.unapply))

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
   * Deletes all menus.
   */
  def deleteAll() = {
    val menus = mongoClient("menumod")("menus")
    menus.drop()
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
   * Loads menu data from a JSON file.
   */
  def load(file: File) {
    val jsonList = Json.parse(Files.readFile(file)).as[List[JsValue]]
    val documents = jsonList.map(menu => JSON.parse(menu.toString).asInstanceOf[DBObject])

//    import scala.collection.JavaConverters._
    val menus = mongoClient("menumod")("menus")
    val result = menus.insert(documents: _*)
    Logger.debug("result %s" format result)
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