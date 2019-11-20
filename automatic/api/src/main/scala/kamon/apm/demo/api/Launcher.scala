package kamon.apm.demo.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import kamon.Kamon
import kamon.apm.demo.api.api.Routes

import scala.concurrent.ExecutionContext


object Launcher extends App {
  Kamon.init()
  val config = ConfigFactory.load()
  val serviceName = config.getString("kamon.environment.service")

  implicit val system: ActorSystem = ActorSystem(serviceName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val routes = Routes.routes(
    bouncerAddress = config.getString("api.services.bouncer"),
    conciergeAddress = config.getString("api.services.concierge"))

  val bindingFuture = Http(system).bindAndHandle(routes, "0.0.0.0", 8090)

}
