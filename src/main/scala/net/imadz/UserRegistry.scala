package net.imadz

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.collection.immutable

// User Case Classes
final case class User(name : String, age: Int, countryOfResidence: String)
final case class Users(users: immutable.Seq[User])

object UserRegistry {
  //Command
  sealed trait Command
  final case class GetUser(name: String, replyTo: ActorRef[GetUserResponse]) extends Command
  final case class GetUsers(replyTo: ActorRef[Users]) extends Command
  final case class CreateUser(user: User, replyTo: ActorRef[ActionPerformed]) extends Command
  final case class DeleteUser(name: String, replyTo: ActorRef[ActionPerformed]) extends Command

  final case class GetUserResponse(maybeUser: Option[User])
  final case class ActionPerformed(msg: String)


  // Behaviors
  def apply(): Behavior[Command] = registry(Set.empty)
  private def registry(users: Set[User]): Behavior[Command] = Behaviors.receiveMessage{
    case GetUser(name, replyTo) =>
      replyTo ! GetUserResponse(users.find(_.name == name))
      Behaviors.empty
    case GetUsers(replyTo) =>
      replyTo ! Users(immutable.Seq(users).flatten)
      Behaviors.empty
    case CreateUser(user, replyTo) =>
      replyTo ! ActionPerformed(s"User ${user.name} created. ")
      registry(users + user)
    case DeleteUser(name, replyTo) =>
      replyTo ! ActionPerformed(s"User $name deleted. ")
      registry(users.filterNot(_.name == name))
  }

}
