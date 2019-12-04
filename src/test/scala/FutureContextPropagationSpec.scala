//import java.util.concurrent.Executors
//
//import kamon.Kamon
//import kamon.instrumentation.futures.scala.ScalaFutureInstrumentation.{
//  Settings,
//  startedSpan,
//  traceBody
//}
//import kamon.testkit.TestSpanReporter
//import kamon.trace.{Identifier, Span, SpanBuilder}
//import org.scalatest.BeforeAndAfter
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AsyncWordSpec
//
//import scala.concurrent.{Await, ExecutionContext, Future}
//import scala.concurrent.duration._
//import scala.util.control.NonFatal
//
//class FutureContextPropagationSpec
//    extends AsyncWordSpec
//    with TestSpanReporter
//    with Matchers
//    with BeforeAndAfter {
//
//  def traceBodyClose[S](operationName: String)(body: => S): S = {
//    val span = Kamon.spanBuilder(operationName).start()
//    val scope =
//      Kamon.storeContext(Kamon.currentContext().withEntry(Span.Key, span))
//
//    try {
//      body
//    } catch {
//      case NonFatal(error) =>
//        span.fail(error.getMessage, error)
//        throw error
//    } finally {
//      span.finish()
//      scope.close()
//    }
//  }
//
//  Kamon.init()
//
//  implicit val ec: ExecutionContext =
//    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
//
//  "traceBody" in {
//    val SPAN0 = "span0"
//    val SPAN1 = "span1"
//    val SPAN2 = "span2"
//    val newSpan = {
//      val builder = Kamon.spanBuilder(SPAN0)
//      builder.start()
//    }
//    val f = Kamon.runWithSpan(newSpan, finishSpan = false) {
//      for {
//        _ <- Future {
//          traceBody(SPAN1)(println("1"))
//        }
//        _ <- Future {
//          traceBody(SPAN2)(println("2"))
//        }
//      } yield newSpan.finish()
//    }
//
//    f.map { _ =>
//      val spans = testSpanReporter().spans(1.seconds)
//      val span0 = spans.find(_.operationName == SPAN0).get
//      val span1 = spans.find(_.operationName == SPAN1).get
//      val span2 = spans.find(_.operationName == SPAN2).get
//
//      span0.parentId shouldBe Identifier.Empty
//      span1.parentId shouldBe span0.id
//      span2.parentId shouldBe span1.id
//    }
//
//  }
//  "traceBodyClose" in {
//    val SPAN0 = "span0"
//    val SPAN1 = "span1"
//    val SPAN2 = "span2"
//    val SPAN3 = "span3"
//    val newSpan = {
//      val builder = Kamon.spanBuilder(SPAN0)
//      builder.start()
//    }
//    val f = Kamon.runWithSpan(newSpan, finishSpan = false) {
//      for {
//        _ <- Future {
//          traceBodyClose(SPAN1)(())
//        }.map { _ =>
//          traceBodyClose(SPAN2)(())
//        }
//        _ <- Future {
//          traceBodyClose(SPAN3)(())
//        }
//      } yield newSpan.finish()
//    }
//
//    f.map { _ =>
//      val spans = testSpanReporter().spans(1.seconds)
//      val span0 = spans.find(_.operationName == SPAN0).get
//      val span1 = spans.find(_.operationName == SPAN1).get
//      val span2 = spans.find(_.operationName == SPAN2).get
//      val span3 = spans.find(_.operationName == SPAN3).get
//
//      span0.parentId shouldBe Identifier.Empty
//      span1.parentId shouldBe span0.id
//      span2.parentId shouldBe span0.id
//      span3.parentId shouldBe span0.id
//    }
//
//  }
//
//  after {
//    testSpanReporter().clear()
//  }
//
//}
