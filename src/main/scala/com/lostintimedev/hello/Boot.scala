package com.lostintimedev.hello

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings}
import akka.event.{Logging, LoggingAdapter}
import com.lostintimedev.hello.actors.TickActor
import com.lostintimedev.hello.actors.TickActor.End
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.{global ⇒ scalaEc}

trait HelloAkka {

  lazy val config: Config = ConfigFactory.load()

  val systemName = "HelloAkka"

  implicit val system: ActorSystem = ActorSystem(systemName, config)

  implicit val executionContext: ExecutionContext = scalaEc

  val tickActor: ActorRef = system.actorOf(
    ClusterSingletonManager.props(
      singletonProps = Props(classOf[TickActor], executionContext),
      terminationMessage = End,
      settings = ClusterSingletonManagerSettings(system).withRole("worker")
    ),
    name = "counter"
  )

  def log: LoggingAdapter = Logging(system, this.getClass)
}

object Boot extends App with HelloAkka {
  log.info(s"server is online")
}
