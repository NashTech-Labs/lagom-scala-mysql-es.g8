package com.knoldus.user.impl.eventsourcing

import java.util.concurrent.atomic.AtomicInteger

import akka.Done
import akka.persistence.query.Sequence
import com.knoldus.user.api.models.UserDetails
import com.knoldus.user.impl.UserApplication
import com.knoldus.user.impl.UserTestHelper.{NewName, OrgID, ValidUserDetails}
import com.knoldus.user.impl.eventSourcing.{UserAdded, UserDeleted, UserEvent, UserUpdated}
import com.knoldus.user.impl.repository.UserRepository
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.testkit.{ReadSideTestDriver, ServiceTest}
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Future

class UserProcessorSpec extends AsyncWordSpec with BeforeAndAfterAll with Matchers {

  private val server = ServiceTest.startServer(ServiceTest.defaultSetup.withJdbc(true)) { ctx =>
    new UserApplication(ctx) {
      override def serviceLocator: ServiceLocator.NoServiceLocator.type = NoServiceLocator

      override lazy val readSide: ReadSideTestDriver = new ReadSideTestDriver
    }
  }

  private val testDriver = server.application.readSide
  private val userRepository: UserRepository = server.application.repository
  private val offset = new AtomicInteger()

  override def afterAll(): Unit = server.stop()

  "The booking event processor" should {
    "add booking" in {
      for {
        _ <- feed(OrgID, UserAdded(ValidUserDetails))
        getUserByID <- getUser(ValidUserDetails.orgId)
      } yield {
        getUserByID.nonEmpty shouldBe true
      }
    }

    "update bookings" in {
      for {
        _ <- feed(OrgID, UserUpdated(OrgID, NewName))
        getUserByID <- getUser(ValidUserDetails.orgId)
      } yield {
        val updatedName = getUserByID.map(userDetails => userDetails.name)
        updatedName shouldBe Some(NewName)
      }
    }

    "delete bookings" in {
      for {
        _ <- feed(OrgID, UserDeleted(OrgID))
        getUserByID <- getUser(ValidUserDetails.orgId)
      } yield {
        getUserByID shouldBe None
      }
    }
  }

  private def getUser(orgId: Int): Future[Option[UserDetails]] = {
    userRepository.getUserById(orgId)
  }

  private def feed(id: Int, event: UserEvent): Future[Done] = {
    testDriver.feed(id.toString, event, Sequence(offset.getAndIncrement))
  }
}
