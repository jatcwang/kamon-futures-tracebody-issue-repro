import java.util.concurrent.{ExecutorService, Executors}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import cats.effect.{ContextShift, IO}
import kamon.Kamon
import kamon.testkit.TestSpanReporter
import kamon.trace.Span
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.{ExecutionContext, Future}

class IOContextPropagation
    extends AsyncWordSpec
    with TestSpanReporter
    with Matchers
    with BeforeAndAfter {

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(15))
  implicit val ioShift: ContextShift[IO] = IO.contextShift(ec)

  "http client " should {
    "pass on trace context" in {
      val httpClient = Http()(ActorSystem("http-actor"))
      val SPAN0 = "span0"
      for {

        expectedSpanId <- Future {
          val span = Kamon.spanBuilder(SPAN0).start()
          val ctx = Kamon.currentContext().withEntry(Span.Key, span)
          //          val span = Kamon.buildSpan(SPAN0).start()
          //          val ctx = Kamon.currentContext().withKey(Span.ContextKey, span)
          Kamon.storeContext(ctx)
          currentSpanId()
        }(ec)
        _ <- httpClient.singleRequest(HttpRequest(uri = "https://example.com"))
        _ <- Future {
          currentSpanId() shouldBe expectedSpanId
        }(ec)
      } yield succeed
    }
  }

  "IO.fromFuture" should {

    "work with with trace propagation" in {

      val httpClient = Http()(ActorSystem("http-actor"))
      val SPAN0 = "span0"
      (for {
        expectedSpanId <- IO {
          val span = Kamon.spanBuilder(SPAN0).start()
          val ctx = Kamon.currentContext().withEntry(Span.Key, span)
//          val span = Kamon.buildSpan(SPAN0).start()
//          val ctx = Kamon.currentContext().withKey(Span.ContextKey, span)
          Kamon.storeContext(ctx)
          span.id.string
        }
        _ <- IO.shift
        _ <- IO {
          withClue("io1") {
            currentSpanId() shouldBe expectedSpanId
          }
        }
        _ <- IO.shift
        _ <- IO.fromFuture(IO {
          withClue("before http") {
            currentSpanId() shouldBe expectedSpanId
          }
          httpClient.singleRequest(HttpRequest(uri = "https://example.com"))
              // Uncomment the following and context propagation suddenly works
//            .map(_ =>
//              withClue("in future map") {
//                currentSpanId() shouldBe expectedSpanId
//              }
//            )(ec)
        })
        _ <- IO.shift
        _ <- IO {
          withClue("io after") {
            currentSpanId() shouldBe expectedSpanId
          }
        }

      } yield succeed).unsafeToFuture()
    }
  }

  private def currentSpanId(): String = {
    Kamon.currentSpan().id.string
//    Kamon.currentSpan().context().spanID.string
  }

}
