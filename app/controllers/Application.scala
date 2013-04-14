package controllers

import play.api.mvc._
import models.{Dish, Menu}
import play.api.data.{Mapping, Form}
import play.api.data.Forms._
import play.api.Logger

object Application extends Controller {

  def index = Action { request =>
    val currentMenuUUID = request.cookies.get("MenuMod").map(_.value).getOrElse("")
    Logger.debug("current menu = %s" format currentMenuUUID)
    Ok(views.html.index(currentMenuUUID))
  }

  /**
   * Create (save) a new menu and redirect to its page.
   */
  def create = Action {
    val menu = Menu.create
    Redirect(routes.Application.show(menu.uuid))
  }

  val dishMapping = mapping(
    "name" -> text,
    "price" -> optional(text)
  )(Dish.apply)(Dish.unapply)

  /**
   * Form definition: the UUID field is ignored, because the action replaces the value from the URL.
   */
  val menuForm = Form(
    mapping(
      "uuid" -> ignored(""),
      "title" -> text,
      "dishes" -> list(dishMapping)
    )(Menu.apply)(Menu.unapply)
  )

  /**
   * Saves changes to a menu.
   *
   */
  def save(uuid: String) = Action { implicit request =>
    menuForm.bindFromRequest.fold(
      form => BadRequest(form.errorsAsJson.toString).as("application/json"),
      menu => {
        Menu.update(menu.copy(uuid = uuid))
        Redirect(routes.Application.show(uuid))
      }
    )
  }

  /**
   * Displays the menu with the given object ID.
   */
  def show(uuid: String) = Action {
    Menu.find(uuid).map { menu =>
      Ok(views.html.show(menu)).withCookies(Cookie("MenuMod", uuid))
    }.getOrElse {
      NotFound("Menu does not exist")
    }
  }
}