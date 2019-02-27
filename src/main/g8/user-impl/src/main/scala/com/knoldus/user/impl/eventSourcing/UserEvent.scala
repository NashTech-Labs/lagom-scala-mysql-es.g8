package com.knoldus.user.impl.eventSourcing

import com.knoldus.user.api.models.UserDetails
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards, AggregateEventTag, AggregateEventTagger}
import play.api.libs.json.{Format, Json}

trait UserEvent extends AggregateEvent[UserEvent] {
  override def aggregateTag: AggregateEventTagger[UserEvent] = UserEvent.Tag
}

object UserEvent {
  val Tag: AggregateEventShards[UserEvent] = AggregateEventTag.sharded[UserEvent](2)
}

case class UserAdded(user: UserDetails) extends UserEvent

object UserAdded {
  implicit val Format: Format[UserAdded] = Json.format
}

case class UserUpdated(orgId: Int, name: String) extends UserEvent

object UserUpdated {
  implicit val Format: Format[UserUpdated] = Json.format
}

case class UserDeleted(orgId: Int) extends UserEvent

object UserDeleted {
  implicit val Format: Format[UserDeleted] = Json.format
}
