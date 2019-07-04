package kamon.apm.demo.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import kamon.apm.demo.api.api.Routes

import scala.concurrent.ExecutionContext


object Launcher extends App {
  val config = ConfigFactory.load()
  val serviceName = config.getString("kamon.environment.service")

  implicit val system: ActorSystem = ActorSystem(serviceName)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val routes = Routes.routes(
    balancerAddress = config.getString("api.balancer.address"),
    prefix = config.getString("api.balancer.prefix"))

  val bindingFuture = Http(system).bindAndHandle(routes, "0.0.0.0", 8080)

}
