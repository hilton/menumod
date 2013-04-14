import models.Menu
import play.api._
import play.api.Play.current

object Global extends GlobalSettings {

  /**
   * Reset the database and load test data on start-up.
   */
  override def onStart(app: Application) {
    Logger.info("Reset database...")
    Menu.deleteAll()
    Menu.load(Play.getFile("conf/test-data.json"))
  }

}
