package net.imadz

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.util.{Failure, Success}

object QuickStartApp {

  private def startHttpServer(route: Route)(implicit system: ActorSystem[Nothing]) = {
    import system.executionContext
    Http().newServerAt("localhost", 8080).bind(route).onComplete{
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Your Server started at: http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Your server cannot bind to the Endpoint, terminating now...", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    val rootBehavior: Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      val registry = context.spawn(UserRegistry(), "user-registry")
      context.watch(registry)
      val route = new UserRoutes(registry)(context.system).route
      startHttpServer(route)(context.system)
      Behaviors.empty
    }
    ActorSystem[Nothing](rootBehavior, "hello-akka-http")
  }

}
