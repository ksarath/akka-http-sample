package com.sample.services

import akka.actor._
import akka.stream.ActorMaterializer
import com.github.swagger.akka._
import com.github.swagger.akka.model.Info

import scala.reflect.runtime.universe._

class SwaggerHttpServiceRoute(val system: ActorSystem, val mat: ActorMaterializer) extends SwaggerHttpService with HasActorSystem {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = mat

  override val apiTypes = Seq(typeOf[AkkaHttpServer], typeOf[CustomerServiceRoute])
  override val basePath = "/" //the basePath for the API you are exposing
  override val apiDocsPath = "api-docs" //where you want the swagger-json endpoint exposed
  override val info = Info(description =
    """Akka Http Sample Rest Services:
      |
      |A sample akka http service
      |
      |""".stripMargin, version = "1.0.0")

  def swaggerUIRoute = get {
    routes ~
      pathPrefix("") {
        pathEndOrSingleSlash {
          getFromResource("swagger-ui/index.html")
        }
      } ~ getFromResourceDirectory("swagger-ui")
  }

}
