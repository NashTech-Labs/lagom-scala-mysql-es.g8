package com.knoldus.user.api.models

import play.api.libs.json.{Json, OFormat}

case class UserDetails(orgId: Int, email: String, name: String)

object UserDetails {
  implicit val UserDetailsFormatter: OFormat[UserDetails] = Json.format[UserDetails]
}
