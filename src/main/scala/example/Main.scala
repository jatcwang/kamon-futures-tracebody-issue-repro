package example

import com.typesafe.scalalogging.StrictLogging
import kamon.Kamon
import kamon.instrumentation.futures.scala.ScalaFutureInstrumentation._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main extends StrictLogging {

  def main(args: Array[String]): Unit = {
    Kamon.init()

    val newSpan = {
      val builder = Kamon.spanBuilder("span0")
      builder.start()
    }
    Kamon.runWithSpan(newSpan) {
      val f = for {
        _ <- Future { traceBody("span1")(println("1")) }
        _ <- Future { traceBody("span2")(println("2")) }
      } yield ()
      Await.result(f, 2.seconds)
    }
  }

}
