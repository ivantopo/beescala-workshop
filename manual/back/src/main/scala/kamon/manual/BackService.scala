package kamon.manual

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer

object BackService extends App {
  implicit val system = ActorSystem("back-service")
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(BackServiceAPI.routes(), "0.0.0.0", 9071)
}

object BackServiceAPI extends Directives with RequestBuilding {

  def routes()(implicit system: ActorSystem): Route = {
    get {
      path("status") {
        complete("ok")
      } ~
      path("hello") {
        complete("Hola amigo mio!")
      }
    }
  }
}


