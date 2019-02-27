package com.knoldus.user.impl.eventsourcing

import java.util.UUID.randomUUID

import akka.Done
import akka.actor.ActorSystem
import akka.actor.setup.ActorSystemSetup
import com.knoldus.user.impl.UserSerializerRegistry
import com.knoldus.user.impl.UserTestHelper.{NewName, OrgID, ValidUserDetails}
import com.knoldus.user.impl.eventSourcing._
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalatest.AsyncWordSpec

class UserEntitySpec extends AsyncWordSpec {

  val system = ActorSystem("UserEntitySpec", ActorSystemSetup(
    JsonSerializerRegistry.serializationSetupFor(UserSerializerRegistry)))

  val uuid: String = randomUUID().toString

  val driver = new PersistentEntityTestDriver(system, new UserEntity, uuid)

  "User Entity" should {

    "return empty User state" in {
      val outcome = driver.run(GetUserCommand(ValidUserDetails.orgId))
      assert(outcome.state.user.isEmpty)
      assert(outcome.issues == Nil)
    }

    "successfully handle AddUserCommand" in {
      val outcome = driver.run(AddUserCommand(ValidUserDetails))
      assert(outcome.events == Vector(UserAdded(ValidUserDetails)))
      assert(outcome.state.user.contains(ValidUserDetails))
      assert(outcome.events.lengthCompare(1) == 0)
      assert(outcome.replies == List(Done))
      assert(outcome.issues == Nil)
    }

    "successfully handle UpdateUserCommand" in {
      driver.run(AddUserCommand(ValidUserDetails))
      val outcome = driver.run(UpdateUserCommand(OrgID, NewName))
      assert(outcome.replies == List(Done))
      assert(outcome.events == Vector(UserUpdated(OrgID, NewName)))
      assert(outcome.state.user.contains(ValidUserDetails.copy(name = NewName)))
      assert(outcome.events.lengthCompare(1) == 0)
      assert(outcome.replies == List(Done))
      assert(outcome.issues == Nil)

    }

    "successfully handle DeleteUserCommand" in {
      driver.run(AddUserCommand(ValidUserDetails))
      val outcome = driver.run(DeleteUserCommand(ValidUserDetails.orgId))
      assert(outcome.replies == List(Done))
      assert(outcome.events == Vector(UserDeleted(OrgID)))
      assert(outcome.state.user.isEmpty)
      assert(outcome.replies == List(Done))
      assert(outcome.issues == Nil)
    }
  }
}
