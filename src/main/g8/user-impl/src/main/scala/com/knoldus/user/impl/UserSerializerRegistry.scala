package com.knoldus.user.impl

import com.knoldus.user.impl.eventSourcing._
import com.knoldus.user.api.models.{GetUserResponse, UpdateRequest, UserDetails, UserResponse}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable.Seq

object UserSerializerRegistry extends JsonSerializerRegistry {

  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[UserResponse],
    JsonSerializer[UserDetails],
    JsonSerializer[AddUserCommand],
    JsonSerializer[GetUserCommand],
    JsonSerializer[UpdateUserCommand],
    JsonSerializer[DeleteUserCommand],
    JsonSerializer[UserAdded],
    JsonSerializer[UserState],
    JsonSerializer[UserUpdated],
    JsonSerializer[UserDeleted],
    JsonSerializer[GetUserResponse],
    JsonSerializer[UpdateRequest]
  )

}
