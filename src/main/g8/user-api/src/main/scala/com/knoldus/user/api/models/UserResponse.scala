package com.knoldus.user.api.models

import play.api.libs.json.{Json, OFormat}

case class UserResponse(message: String)

object UserResponse {
  implicit val UserResponseFormatter: OFormat[UserResponse] = Json.format[UserResponse]
}
