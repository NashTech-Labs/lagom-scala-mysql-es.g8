package com.knoldus.user.impl.eventSourcing

import akka.Done
import com.knoldus.user.api.models.UserDetails
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

trait UserCommand[R] extends ReplyType[R]

case class AddUserCommand(user: UserDetails) extends UserCommand[Done]

object AddUserCommand {
  implicit val Format: Format[AddUserCommand] = Json.format
}

case class GetUserCommand(orgId: Int) extends UserCommand[Option[UserDetails]]

object GetUserCommand {
  implicit val Format: Format[GetUserCommand] = Json.format
}

case class UpdateUserCommand(orgId: Int, name: String) extends UserCommand[Done]

object UpdateUserCommand {
  implicit val Format: Format[UpdateUserCommand] = Json.format
}

case class DeleteUserCommand(orgId: Int) extends UserCommand[Done]

object DeleteUserCommand {
  implicit val Format: Format[DeleteUserCommand] = Json.format
}

