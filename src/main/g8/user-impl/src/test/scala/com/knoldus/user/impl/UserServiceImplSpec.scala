package com.knoldus.user.impl

import com.knoldus.user.api.models.UserResponse
import com.knoldus.user.impl.UserTestHelper.{OrgID, UserUpdateRequest, ValidUserDetails}
import com.knoldus.user.impl.constants.UserConstants._
import com.knoldus.user.impl.dataprocessor.UserEsService
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import org.mockito.Mockito
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserServiceImplSpec extends WordSpec with Matchers {

  val persistentEntityRegistry: PersistentEntityRegistry = MockitoSugar.mock[PersistentEntityRegistry]
  val esService: UserEsService = MockitoSugar.mock[UserEsService]
  val userServiceImpl: UserServiceImpl = new UserServiceImpl(esService)

  "addUser" should {
    "return success response" in {
      val response = UserResponse(AddedSuccessfully)
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(None))
      Mockito.when(esService.processUserAdded(ValidUserDetails)).thenReturn(Future.successful(response))
      val result = Await.result(userServiceImpl.addUser().invoke(ValidUserDetails), 5 seconds)
      result.message shouldBe AddedSuccessfully
    }

    "return error response" in {
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(Some(ValidUserDetails)))
      val result = Await.result(userServiceImpl.addUser().invoke(ValidUserDetails), 5 seconds)
      result.message shouldBe UserAlreadyExists
    }
  }

  "updateUser" should {
    "return success response" in {
      val response = UserResponse(UpdatedSuccessfully)
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(Some(ValidUserDetails)))
      Mockito.when(esService.processUserUpdated(UserUpdateRequest)).thenReturn(Future.successful(response))
      val result = Await.result(userServiceImpl.updateUser().invoke(UserUpdateRequest), 5 seconds)
      result.message shouldBe UpdatedSuccessfully
    }

    "return error response" in {
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(None))
      val result = Await.result(userServiceImpl.updateUser().invoke(UserUpdateRequest), 5 seconds)
      result.message shouldBe InvalidUser
    }
  }

  "deleteUser" should {
    "return success response" in {
      val response = UserResponse(DeletedSuccessfully)
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(Some(ValidUserDetails)))
      Mockito.when(esService.processUserDeleted(OrgID)).thenReturn(Future.successful(response))
      val result = Await.result(userServiceImpl.deleteUser(OrgID).invoke(), 5 seconds)
      result.message shouldBe DeletedSuccessfully
    }

    "return error response" in {
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(None))
      val result = Await.result(userServiceImpl.deleteUser(OrgID).invoke(), 5 seconds)
      result.message shouldBe InvalidUser
    }
  }

  "getUser" should {
    "return success response" in {
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(Some(ValidUserDetails)))
      val result = Await.result(userServiceImpl.getUser(OrgID).invoke(), 5 seconds)
      result.details.isDefined shouldBe true
      result.message.isDefined shouldBe false
    }

    "return error response" in {
      Mockito.when(esService.getUserDetails(OrgID)).thenReturn(Future.successful(None))
      val result = Await.result(userServiceImpl.getUser(OrgID).invoke(), 5 seconds)
      result.details.isDefined shouldBe false
      result.message.isDefined shouldBe true
    }
  }
}
