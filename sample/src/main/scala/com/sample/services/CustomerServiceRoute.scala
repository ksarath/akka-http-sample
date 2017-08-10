package com.sample.services

import java.util.UUID
import javax.ws.rs.Path

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{ Directives, Route }
import akka.util.Timeout
import io.swagger.annotations._

@Api(value = "Sample Service", produces = "application/json", consumes = "application/json")
@Path("/customer")
class CustomerServiceRoute()(implicit system: ActorSystem) extends SprayJsonSupport {

  import Directives._
  import ServiceMetaInfo._

  import scala.concurrent.duration._

  val logger = Logging(system, this.getClass.getSimpleName)

  implicit val timeout = Timeout(20 seconds)

  val route = pathPrefix("customer") {
    getCustomer
  }

  @Path("/{id}")
  @ApiOperation(value = "Get Customer", nickname = "getCustomer", httpMethod = "GET", produces = "text/plain", response = classOf[String])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", paramType = "path", value = "Unique id representing user", required = true,
      dataType = "string", example = "one")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "customer name", response = classOf[String]),
    new ApiResponse(code = 400, message = "Bad Request")
  ))
  def getCustomer: Route = get {
    path(Segment) { customerId =>
      customerId match {
        case "one" => complete(StatusCodes.OK, "customerOne")
        case "two" => complete(StatusCodes.OK, "customer2")
        case _ => complete(StatusCodes.InternalServerError, ErrorResponse("No matching customer"))
      }
    }
  }
}
