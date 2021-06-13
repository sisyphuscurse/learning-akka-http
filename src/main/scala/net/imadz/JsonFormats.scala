package net.imadz

import net.imadz.UserRegistry.ActionPerformed
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object JsonFormats {

  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat3(User)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users)
  implicit val actionPerformedJsonFormat: RootJsonFormat[ActionPerformed] = jsonFormat1(ActionPerformed)

}
