package com.sample.services

import akka.actor._
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import spray.json._

import scala.concurrent._
import scala.util.{ Failure, Success }

/**
 * This service wires all provided service routes together and ensures proper handling issues in the requests
 */
class AkkaHttpServer()(implicit system: ActorSystem, executor: ExecutionContextExecutor, materializer: ActorMaterializer) extends SprayJsonSupport {

  import Directives._
  import ServiceMetaInfo._

  val logger = Logging(system, this.getClass.getSimpleName)

  def log(e: Throwable)(handle: Route): Route = rc => {
    logger.warning(s"Request ${rc.request} could not be handled normally. Cause: ${e.getMessage}")
    handle(rc)
  }

  def defaultExceptionHandler(e: Throwable): Route =
    log(e) {
      badRequest(e.getMessage)
    }

  def badRequest(msg: String): Route = {
    rc => rc.complete(StatusCodes.BadRequest, ErrorResponse(msg))
  }

  def notAuthorizedHandler(e: Throwable): Route =
    log(e) {
      rc => rc.complete(StatusCodes.Unauthorized, ErrorResponse(e.getMessage))
    }

  implicit val requestServiceExceptionHandler = ExceptionHandler {
    case e: DeserializationException => defaultExceptionHandler(e)
    case e: java.lang.SecurityException => notAuthorizedHandler(e)
    case e: Exception => defaultExceptionHandler(e)
  }

  implicit def requestServiceRejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case MalformedRequestContentRejection(errorMessage, cause) => complete(StatusCodes.BadRequest, ErrorResponse(errorMessage))
      }
      .handle {
        case AuthorizationFailedRejection ⇒ complete(StatusCodes.Forbidden, ErrorResponse("Not Authorized"))
      }
      .handle {
        case ValidationRejection(msg, _) ⇒ complete((StatusCodes.InternalServerError, "Internal error due to: " + msg))
      }
      .handleAll[MethodRejection] {
        methodRejections ⇒
          val names = methodRejections.map(_.supported.name)
          complete(StatusCodes.MethodNotAllowed, ErrorResponse(s"Can't do that! Supported: ${names mkString " or "}!"))
      }
      .handleNotFound { complete(StatusCodes.NotFound, ErrorResponse("Not here!")) }
      .result()

  // API documentation frontend.
  val swaggerService = new SwaggerHttpServiceRoute(system, materializer)

  // Customer Service Route
  val customerServiceRoute = new CustomerServiceRoute()

  val route = {
    customerServiceRoute.route ~ swaggerService.swaggerUIRoute
  }

  def runServer(host: String, port: Int) = {
    val httpServer = Http().bindAndHandle(route, host, port)

    httpServer onComplete {
      case Success(answer) ⇒ logger.info("service is available: " + answer)
      case Failure(msg) ⇒ logger.error("service failed: " + msg)
    }
  }

}