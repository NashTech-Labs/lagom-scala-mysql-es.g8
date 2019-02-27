package com.knoldus.user.impl.dataprocessor

import akka.Done
import com.knoldus.user.api.models.{UpdateRequest, UserDetails, UserResponse}
import com.knoldus.user.impl.constants.UserConstants.{AddedSuccessfully, DeletedSuccessfully, UpdatedSuccessfully}
import com.knoldus.user.impl.eventSourcing._
import com.lightbend.lagom.scaladsl.persistence.{PersistentEntityRef, PersistentEntityRegistry}

import scala.concurrent.{ExecutionContext, Future}

class UserEsService(persistentEntityRegistry: PersistentEntityRegistry)(implicit ec: ExecutionContext) {

  def processUserAdded(userDetails: UserDetails): Future[UserResponse] = {
    ref(userDetails.orgId.toString).ask(AddUserCommand(userDetails)).map {
      case Done => UserResponse(AddedSuccessfully)
    }
  }

  def processUserUpdated(request: UpdateRequest): Future[UserResponse] = {
    ref(request.orgId.toString).ask(UpdateUserCommand(request.orgId, request.name)).map {
      case Done => UserResponse(UpdatedSuccessfully)
    }
  }

  def processUserDeleted(orgId: Int): Future[UserResponse] = {
    ref(orgId.toString).ask(DeleteUserCommand(orgId)).map {
      case Done => UserResponse(DeletedSuccessfully)
    }
  }

  def ref(id: String): PersistentEntityRef[UserCommand[_]] = persistentEntityRegistry.refFor[UserEntity](id)

  def getUserDetails(orgId: Int): Future[Option[UserDetails]] = {
    ref(orgId.toString).ask(GetUserCommand(orgId))
  }
}
