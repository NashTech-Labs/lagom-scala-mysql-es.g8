package com.knoldus.user.impl.eventSourcing

import java.time.LocalDateTime

import akka.Done
import com.knoldus.user.api.models.UserDetails
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class UserEntity extends PersistentEntity {

  override type Command = UserCommand[_]
  override type Event = UserEvent
  override type State = UserState

  private val userAction: Actions = Actions()
    .onCommand[AddUserCommand, Done] {
    case (AddUserCommand(user), ctx, _) =>
      ctx.thenPersist(UserAdded(user))(_ => ctx.reply(Done))
  }
    .onCommand[UpdateUserCommand, Done] {
    case (UpdateUserCommand(orgId, name), ctx, _) =>
      ctx.thenPersist(UserUpdated(orgId, name))(_ => ctx.reply(Done))
  }
    .onCommand[DeleteUserCommand, Done] {
    case (DeleteUserCommand(orgId), ctx, _) =>
      ctx.thenPersist(UserDeleted(orgId))(_ => ctx.reply(Done))
  }
    .onReadOnlyCommand[GetUserCommand, Option[UserDetails]] {
    case (GetUserCommand(_), ctx, state) =>
      ctx.reply(state.user)
  }
    .onEvent {
      case (UserAdded(user), _) => UserState(Some(user), LocalDateTime.now().toString)
      case (UserUpdated(orgId, username), state: UserState) =>
        val user = state.user.fold(UserDetails(orgId, "", "")) {
          details => details.copy(name = username)
        }
        UserState(Some(user), LocalDateTime.now().toString)
      case (UserDeleted(_), _) => UserState(None, LocalDateTime.now().toString)
    }

  override def initialState: UserState = UserState.startingState

  override def behavior: Behavior = {
    case UserState(_, _) => userAction
  }
}
