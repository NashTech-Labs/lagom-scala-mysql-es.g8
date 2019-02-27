package com.knoldus.user.impl.eventSourcing

import akka.stream.scaladsl.Flow
import akka.{Done, NotUsed}
import com.knoldus.user.impl.repository.UserRepository
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor.ReadSideHandler
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, EventStreamElement, ReadSideProcessor}

import scala.concurrent.Future

class UserProcessor(repository: UserRepository) extends ReadSideProcessor[UserEvent] {

  override def buildHandler(): ReadSideProcessor.ReadSideHandler[UserEvent] = {
    new ReadSideHandler[UserEvent] {

      override def globalPrepare(): Future[Done] = repository.createTable

      override def handle(): Flow[EventStreamElement[UserEvent], Done, NotUsed] = {
        Flow[EventStreamElement[UserEvent]].mapAsync(4) { eventElement =>
          eventElement.event match {
            case addEvent: UserAdded => repository.processUserAdded(addEvent)
            case updateEvent: UserUpdated => repository.processUserUpdated(updateEvent)
            case deleteEvent: UserDeleted => repository.processUserDeleted(deleteEvent)
          }
        }
      }
    }
  }

  override def aggregateTags: Set[AggregateEventTag[UserEvent]] = UserEvent.Tag.allTags

}
