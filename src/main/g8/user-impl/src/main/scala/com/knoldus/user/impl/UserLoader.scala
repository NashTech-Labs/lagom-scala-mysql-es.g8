package com.knoldus.user.impl

import com.knoldus.user.api.UserService
import com.knoldus.user.impl.dataprocessor.UserEsService
import com.knoldus.user.impl.eventSourcing.{UserEntity, UserProcessor}
import com.knoldus.user.impl.repository.{UserRepository, UserRepositoryImpl}
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.api.{Descriptor, ServiceLocator}
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.jdbc.JdbcPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.db.HikariCPComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.filters.cors.CORSComponents

class UserLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new UserApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new UserApplication(context) with LagomDevModeComponents

  override def describeService: Some[Descriptor] = Some(readDescriptor[UserService])

}

abstract class UserApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with CORSComponents
    with HikariCPComponents
    with JdbcPersistenceComponents {

  override lazy val lagomServer: LagomServer = serverFor[UserService](wire[UserServiceImpl])
  override lazy val jsonSerializerRegistry: UserSerializerRegistry.type = UserSerializerRegistry

  persistentEntityRegistry.register(wire[UserEntity])
  lazy val repository: UserRepository = wire[UserRepositoryImpl]
  lazy val esService: UserEsService = wire[UserEsService]

  readSide.register(wire[UserProcessor])

}
