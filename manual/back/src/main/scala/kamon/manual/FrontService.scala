package kamon.manual

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}

object FrontService extends App {
  implicit val system = ActorSystem("front-service")
  implicit val materializer = ActorMaterializer()

  Http().bindAndHandle(FrontServiceAPI.routes(), "0.0.0.0", 9070)
}

object FrontServiceAPI extends Directives with RequestBuilding {

  def routes()(implicit system: ActorSystem): Route = {
    import system.dispatcher

    get {
      path("status") {
        complete("ok")
      } ~
      path("future") {
        complete(future())
      } ~
      path("external") {
        parameter('times.as[Int]) { times =>
          complete(external(times))
        }
      }
    }
  }

  private def future()(implicit ec: ExecutionContext): Future[String] = {
    Future("Hello there, this is the actual work done by the future!")
      .map(removePunctuation)
      .map(countWords)
      .map(encodeCount)
  }

  private def removePunctuation(input: String): String = {
    input.filter(_.isLetterOrDigit)
  }

  private def countWords(input: String): Long = {
    input.split(" ").length
  }

  private def encodeCount(input: Long): String = {
    input.toString
  }

  private def external(times: Int)(implicit system: ActorSystem): Future[String] = {
    import system.dispatcher
    val responses = for(_ <- 1 to times) yield {
      Http()
        .singleRequest(Get("http://localhost:9071/hello"))
        .map(r => r.entity.toString)
    }

    Future.sequence(responses).map(_ => "done")
  }
}


