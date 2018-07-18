package vertx.scala.myapp

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.RoutingContext
import io.vertx.scala.ext.web.handler.BodyHandler

import scala.concurrent.Future

class HttpVerticle extends ScalaVerticle {

  override def startFuture(): Future[_] = {
    //Create a router to answer GET-requests to "/hello" with "world"
    val router = Router.router(vertx)

    val homeRoute = router
      .get("/")
      .handler(_.response().end("<h1>My page :</h1>" +
        "<input type=text></input><button>valider</button>"))

    val route = router
      .get("/hello")
      .handler(_.response().end("world"))

    val testRoute = router
      .get("/test")
      .handler(BodyHandler.create())
      .handler( message => vertx.eventBus().sendFuture[String]("myTest", message.getBodyAsString().get).map(
        messageReply => message.response().end(messageReply.body)
      ))

    val wordRoute = router
      .post("/word")
      .handler(BodyHandler.create())
      .handler( message => vertx.eventBus().sendFuture[String]("word", message.request().getFormAttribute("word").get)
        .map(
        messageReply => message.response().end(messageReply.body)
      ))

    vertx
      .createHttpServer()
      .requestHandler(router.accept _)
      .listenFuture(8666, "0.0.0.0")
      .map { httpServer =>
        println(s"""httpServer.isMetricsEnabled: ${ httpServer.isMetricsEnabled }
           |httpServer connected on port: ${ httpServer.actualPort }
           |""".stripMargin)
      }
  }
}
