package vertx.scala.myapp

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message
import java.sql.{Connection, DriverManager, ResultSet};

import scala.concurrent.Future

class BusVerticle extends ScalaVerticle {

  override def startFuture(): Future[_] = {
    vertx
      .eventBus()
      .consumer[String]("testAddress")
      .handler(_.reply("Hello World!!"))
      .completionFuture()

    vertx
      .eventBus()
      .consumer[String]("myTest")
      .handler(_.reply(message = "coucou"))
      .completionFuture()

    vertx
      .eventBus()
      .consumer[String]("word")
      .handler(executeBodyMessage)
      .completionFuture()
  }

  def executeBodyMessage(message: Message[String]) = {
    message.body().toString match {
      case "" => message.reply("no words found")
      case sentText =>
        persistText(sentText)
        message.reply("nice word: " + sentText)
    }
  }

  def persistText(sentText: String) = {
    val configuration = {
      "host" -> "localhost",
      "port" -> "5432",
      "username" -> "postgres",
      "password" -> "mysecretpassword",
      "database" -> "vertxBase"
    }

    println("Postgres connector")

    classOf[org.postgresql.Driver]

    val dbc = "jdbc:postgresql://localhost:3306/vertxBase?user=postgres&password=mysecretpassword"
    val conn = DriverManager.getConnection(dbc)
    try {
      val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

      val rs = stm.executeQuery("SELECT * from Users")

      while(rs.next) {
        println(rs.getString("quote"))
      }
    } finally {
      conn.close()
    }
  }
}
