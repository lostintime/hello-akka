package com.lostintimedev.hello

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import com.typesafe.config.{Config, ConfigFactory}

trait HelloAkka extends App {

  lazy val config: Config = ConfigFactory.load()

  val systemName = "HelloAkka"

  implicit def actorSystem: ActorSystem = ActorSystem(systemName, config)

  def log: LoggingAdapter = Logging(actorSystem, this.getClass)
}

object Boot extends HelloAkka {
  log.info(s"server is online")
}
