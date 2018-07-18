import io.vertx.lang.scala.{ScalaVerticle, VertxExecutionContext}
import io.vertx.scala.core.Vertx
import vertx.scala.myapp.{BusVerticle, HttpVerticle}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object MainApp extends App {
  val vertx = Vertx.vertx()

  implicit val executionContext: ExecutionContext = VertxExecutionContext(vertx.getOrCreateContext())

  vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[HttpVerticle]) onComplete {
    case Success(id) =>
      println(s"The main verticle is loaded. It's ID is $id")
    case Failure(error) =>
      println("An error has occurred on the main verticle loading", error)
      sys.exit(1)
  }

  vertx.deployVerticleFuture(ScalaVerticle.nameForVerticle[BusVerticle]) onComplete {
    case Success(id) =>
      println(s"The BusVerticle is loaded. It's ID is $id")
    case Failure(error) =>
      println("An error has occurred on the main verticle loading", error)
      sys.exit(1)
  }
}
