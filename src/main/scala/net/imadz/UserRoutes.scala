package net.imadz

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import net.imadz.UserRegistry._

import scala.concurrent.Future

class UserRoutes(registry: ActorRef[Command])(implicit system: ActorSystem[Nothing]) {

  import JsonFormats._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  private implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))

  def getUsers: Future[Users] = registry ? GetUsers

  def createUser(user: User): Future[ActionPerformed] = registry ? (CreateUser(user, _))

  def getUser(name: String): Future[GetUserResponse] = registry ? (GetUser(name, _))

  def deleteUser(name: String): Future[ActionPerformed] = registry ? (DeleteUser(name, _))

  val route: Route = pathPrefix("users") {
    concat(
      pathEnd(
        concat(
          get {
            complete(getUsers)
          },
          post {
            entity(as[User]) { user =>
              onSuccess(createUser(user)) { actionPerformed =>
                complete((StatusCodes.Created, actionPerformed))
              }
            }
          }
        )
      ), path(Segment) { name =>
        concat(
          get {
            rejectEmptyResponse {
              onSuccess(getUser(name)) { response =>
                complete(response.maybeUser)
              }
            }
          },
          delete {
            onSuccess(deleteUser(name)) { actionPerformed =>
              complete((StatusCodes.OK, actionPerformed))
            }
          }
        )
      }
    )
  }

}
