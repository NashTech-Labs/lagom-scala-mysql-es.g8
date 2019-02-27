package com.knoldus.user.impl

import akka.NotUsed
import com.knoldus.user.api.UserService
import com.knoldus.user.api.models._
import com.knoldus.user.impl.constants.UserConstants._
import com.knoldus.user.impl.dataprocessor.UserEsService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.{ExecutionContext, Future}

class UserServiceImpl(esService: UserEsService)(implicit ec: ExecutionContext) extends UserService {

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def addUser(): ServiceCall[UserDetails, UserResponse] = {
    request =>
      logger.info(s"Request received to add user details for ID: ${request.orgId}")
      esService.getUserDetails(request.orgId).flatMap {
        case Some(_) => logger.error(s"Failed to add details as user already exists: ${request.orgId}")
          Future.successful(UserResponse(UserAlreadyExists))
        case None => esService.processUserAdded(request)
      }
  }

  override def updateUser(): ServiceCall[UpdateRequest, UserResponse] = {
    request =>
      logger.info(s"Request received to update user name for ID: ${request.orgId}")
      esService.getUserDetails(request.orgId).flatMap {
        case Some(_) => esService.processUserUpdated(request)
        case None => logger.error(s"Failed to update user name for invalid ID: ${request.orgId}")
          Future.successful(UserResponse(InvalidUser))
      }
  }


  override def getUser(orgId: Int): ServiceCall[NotUsed, GetUserResponse] = {
    _ =>
      logger.info(s"Request received to get user details for ID: $orgId")
      esService.getUserDetails(orgId).map {
        case Some(user) => GetUserResponse(Some(user), None)
        case None => logger.error(s"Failed to get user details for invalid ID: $orgId")
          GetUserResponse(None, Some(InvalidUser))
      }
  }

  override def deleteUser(orgId: Int): ServiceCall[NotUsed, UserResponse] = {
    _ =>
      logger.info(s"Request received to delete user for ID: $orgId")
      esService.getUserDetails(orgId).flatMap {
        case Some(user) => esService.processUserDeleted(user.orgId)
        case None => logger.error(s"Failed to delete user for invalid ID: $orgId")
          Future.successful(UserResponse(InvalidUser))
      }
  }
}
