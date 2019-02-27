package com.knoldus.user.impl

import com.knoldus.user.api.models.{UpdateRequest, UserDetails}

object UserTestHelper {

  val OrgID = 1
  val ID = "1"
  val EmailID = "knoldus@gmail.com"
  val Name = "Knoldus"
  val NewName = "Knoldus 2019"
  val ValidUserDetails = new UserDetails(OrgID, EmailID, Name)
  val UserUpdateRequest = new UpdateRequest(OrgID, NewName)

}
