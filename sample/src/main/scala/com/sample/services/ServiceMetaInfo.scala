package com.sample.services

import spray.json.DefaultJsonProtocol

object ServiceMetaInfo extends DefaultJsonProtocol {
  case class ErrorResponse(msg: String)

  implicit val errorResponseFmt = jsonFormat1(ErrorResponse)
}
