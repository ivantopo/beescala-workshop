package controllers

import components.KeyStorage
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
 * Handles API key authentication requests
 */
@Singleton
class KeysController @Inject()(keyStorage: KeyStorage, cc: ControllerComponents, ec: ExecutionContext) extends AbstractController(cc) {
  implicit val iec = ec

  /**
    * Gets a key from storage
    */
  def get(id: String) = Action.async { implicit request: Request[AnyContent] =>
    spiceUp {
      keyStorage.retrieve(id).map(keyOption => {
        keyOption
          .map(key => Ok(keyToJson(key)))
          .getOrElse(Unauthorized)
      })
    }
  }
}
