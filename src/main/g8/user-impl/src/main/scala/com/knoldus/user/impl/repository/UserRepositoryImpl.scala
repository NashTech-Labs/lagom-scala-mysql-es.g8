package com.knoldus.user.impl.repository

import akka.Done
import com.knoldus.user.api.models.UserDetails
import com.knoldus.user.impl.eventSourcing.{UserAdded, UserDeleted, UserUpdated}
import com.lightbend.lagom.scaladsl.persistence.jdbc.JdbcSession

import scala.concurrent.{ExecutionContext, Future}

class UserRepositoryImpl(session: JdbcSession)(implicit ec: ExecutionContext) extends UserRepository {

  override def createTable: Future[Done] = {
    val query =
      """
        |CREATE TABLE IF NOT EXISTS user (
        |orgId INT NOT NULL,
        |email VARCHAR(64) NOT NULL,
        |name VARCHAR(64) NOT NULL,
        |PRIMARY KEY (orgId))
      """.stripMargin
    session.withConnection(_.prepareStatement(query).execute()).map(_ => Done)
  }

  override def processUserAdded(userAddedEvent: UserAdded): Future[Done] = {
    session.withConnection { connection =>
      val statement = connection.prepareStatement("INSERT INTO user (orgId, email, name) VALUES (?, ?, ?)")
      statement.setInt(1, userAddedEvent.user.orgId)
      statement.setString(2, userAddedEvent.user.email)
      statement.setString(3, userAddedEvent.user.name)
      statement.execute()
    }.map(_ => Done)
  }

  override def processUserUpdated(userUpdateEvent: UserUpdated): Future[Done] = {
    session.withConnection { connection =>
      val statement = connection.prepareStatement("UPDATE user SET name = ? WHERE orgId = ?")
      statement.setString(1, userUpdateEvent.name)
      statement.setInt(2, userUpdateEvent.orgId)
      statement.executeUpdate()
    }.map(_ => Done)
  }

  override def processUserDeleted(userDeleteEvent: UserDeleted): Future[Done] = {
    session.withConnection { connection =>
      val statement = connection.prepareStatement("DELETE FROM user WHERE orgId = ?")
      statement.setInt(1, userDeleteEvent.orgId)
      statement.executeUpdate()
    }.map(_ => Done)
  }

  override def getUserById(orgId: Int): Future[Option[UserDetails]] = {
    session.withConnection { connection =>
      val statement = connection.prepareStatement(s"SELECT * FROM user WHERE orgId = $orgId").executeQuery()
      if (statement.next() && orgId.equals(statement.getInt(1))) {
        Some(UserDetails(statement.getInt(1), statement.getString(2),
          statement.getString(3)))
      } else {
        None
      }
    }
  }
}
