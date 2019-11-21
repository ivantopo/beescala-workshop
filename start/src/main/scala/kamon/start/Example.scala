package kamon.start

import kamon.Kamon
import kamon.context.Context

object Example extends App {
  val context = Context.of("request.id", "1234").withTag("name", "topo")
  val span = Kamon.spanBuilder("topospan")

  println("Before: " + Kamon.currentContext())
  Kamon.runWithContext(context) {
    println("Inside: " + Kamon.currentContext())
  }
  println("After: " + Kamon.currentContext())
}
