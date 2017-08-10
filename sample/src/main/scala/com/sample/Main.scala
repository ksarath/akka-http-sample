package com.sample

import akka.actor.ActorSystem
import akka.event.{ Logging, LoggingAdapter }
import akka.stream.ActorMaterializer
import com.sample.services.AkkaHttpServer

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App with Configuration {
  implicit val system = ActorSystem("akka-http-sample-app-actor-system")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val host = config.getString("akka.http.server.bind.host")
  val port = config.getInt("akka.http.server.bind.port")

  implicit val logger: LoggingAdapter = Logging(system, getClass)

  sys addShutdownHook {
    logger.info("Shutting down the akka http server")
    Await.result(system.terminate(), 20 seconds)
  }

  new AkkaHttpServer().runServer(host, port)
}
