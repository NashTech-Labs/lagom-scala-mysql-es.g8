package com.knoldus.user.impl.eventSourcing

import java.time.LocalDateTime

import com.knoldus.user.api.models.UserDetails
import play.api.libs.json.{Format, Json}

case class UserState(user: Option[UserDetails], timeStamp: String)

object UserState {
  implicit val Format: Format[UserState] = Json.format

  def startingState: UserState = UserState(None, LocalDateTime.now().toString)
}
