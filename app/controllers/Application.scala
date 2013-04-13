package controllers

import play.api.mvc._
import models.Menu
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger

object Application extends Controller {

  def index = Action { request =>
    val currentMenuUUID = request.cookies("MenuMod").value
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

  val menuForm = Form(
    mapping(
      "uuid" -> text,
      "title" -> text
    )(Menu.apply)(Menu.unapply)
  )

  /**
   * Saves changes to a menu.
   */
  def save(uuid: String) = Action { implicit request =>
    Menu.update(menuForm.bindFromRequest.get)
    Redirect(routes.Application.show(uuid))
  }

  /**
   * Displays the menu with the given object ID.
   */
  def show(uuid: String) = Action {
    Menu.find(uuid).map { menu =>
      Ok(views.html.show(menu)).withCookies(Cookie("MenuMod", uuid))
    }.getOrElse {
      NotFound
    }
  }
}