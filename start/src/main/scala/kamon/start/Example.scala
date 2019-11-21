package kamon.start

import java.time.Duration
import java.util.concurrent.Executors

import kamon.Kamon
import kamon.context.Context
import kamon.metric.MeasurementUnit
import kamon.tag.Lookups._
import kamon.trace.Span

import scala.concurrent.ExecutionContext

object Example extends App {
  //  val MyContextEntryKey = Context.key

  val span = Kamon.spanBuilder("Hello").start()
  val context = Context.of("request.id", "1234")
    .withTag("name", "topo")
    .withEntry(Span.Key, span)

//  implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
  println("Before: " + Kamon.currentContext())
  Kamon.runWithContext(context) { // a'la ThreadLocal
    //    Future {
    val ctx = Kamon.currentContext()
    println("Inside: " + ctx)
    println("Request ID: " + ctx.getTag(plain("request.id")))
    //    }
  }
  println("After: " + Kamon.currentContext())

  val MyCounter = Kamon.counter("my-counter")

  MyCounter.withTag("direction", "up").increment()
  MyCounter.withTag("direction", "down").increment()

  val MyGauge = Kamon.gauge("myGauge",
    "this is how I gauge things",
    MeasurementUnit.time.nanoseconds)

  val y = MyGauge.withoutTags()
  y.autoUpdate(g => {
    g.update(100)
    println("Updated")
  }, Duration.ofSeconds(1))


  val MyHistogram = Kamon.histogram("whatever").withoutTags()
  MyHistogram.record(100)

//  Thread.sleep(10000)

//  Kamon.stopModules()
//  ec.shutdown()

  Kamon.scheduler().shutdown()


}
