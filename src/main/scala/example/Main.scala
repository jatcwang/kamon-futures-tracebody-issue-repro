package example

import ch.qos.logback.core.LayoutBase
import ch.qos.logback.classic.spi.ILoggingEvent
import com.typesafe.scalalogging.StrictLogging
import kamon.Kamon
import kamon.trace.Span
import kamon.context.Storage
import kamon.context.Context.Key
import java.nio.file.{Files, Paths}

object Main extends StrictLogging {

  def main(args: Array[String]): Unit = {
    Kamon.init()

    val newSpan = {
      val builder = Kamon.spanBuilder("in child_0")
      builder.start()
    }
    val scope = Kamon.currentContext().withEntry(Span.Key, newSpan)
    val newContext = Kamon.storeContext(scope)
    Kamon.runWithContextEntry(
      new Key[String]("logData", ""),
      "myData"
    ) {
      logger.info("in child_0")
    }

    newSpan.finish()
    newContext.close()
  }

}

class MyLayout extends LayoutBase[ILoggingEvent] {

  override def doLayout(event: ILoggingEvent): String = {
    val currentContext = Kamon.currentContext()
    val currentSpan = Kamon.currentSpan()

    val operationName = currentSpan.operationName()

    "log OperationName: " + operationName + "\n"
  }
}
