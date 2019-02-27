package com.knoldus.user.api

import akka.NotUsed
import com.knoldus.user.api.models._
import com.lightbend.lagom.scaladsl.api.Service.{named, restCall}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.transport.Method._
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceAcl, ServiceCall}

trait UserService extends Service {

  override final def descriptor: Descriptor = {

    named("user-service")
      .withCalls(
        restCall(POST, "/user/add", addUser _),
        restCall(GET, "/user/get?orgId", getUser _),
        restCall(PUT, "/user/update", updateUser _),
        restCall(DELETE, "/user/delete?orgId", deleteUser _)
      ).withAutoAcl(true).withAcls(
      ServiceAcl.forMethodAndPathRegex(Method.OPTIONS, "/user.*")
    )
  }

  def addUser(): ServiceCall[UserDetails, UserResponse]

  def updateUser(): ServiceCall[UpdateRequest, UserResponse]

  def getUser(orgId: Int): ServiceCall[NotUsed, GetUserResponse]

  def deleteUser(orgId: Int): ServiceCall[NotUsed, UserResponse]

}
