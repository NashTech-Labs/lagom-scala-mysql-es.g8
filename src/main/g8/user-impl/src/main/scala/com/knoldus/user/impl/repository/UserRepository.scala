package com.knoldus.user.impl.repository

import akka.Done
import com.knoldus.user.api.models.UserDetails
import com.knoldus.user.impl.eventSourcing.{UserAdded, UserDeleted, UserUpdated}

import scala.concurrent.Future

trait UserRepository {

  def createTable: Future[Done]

  def processUserAdded(userAddEvent: UserAdded): Future[Done]

  def processUserUpdated(userUpdateEvent: UserUpdated): Future[Done]

  def processUserDeleted(userDeleteEvent: UserDeleted): Future[Done]

  def getUserById(orgId: Int): Future[Option[UserDetails]]

}
