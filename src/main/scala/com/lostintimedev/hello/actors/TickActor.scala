package com.lostintimedev.hello.actors

import akka.actor.Actor
import akka.event.Logging

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class TickActor(implicit s: ExecutionContext) extends Actor {
  val log = Logging(context.system, this)

  import com.lostintimedev.hello.actors.TickActor._
  import context._

  private val initialTimeout = 5.seconds
  private val reindexTimeout = 10.seconds

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    system.scheduler.schedule(initialTimeout, reindexTimeout, self, Tick)(s)
    ()
  }

  def tac: Receive = {
    case Tick ⇒
      become(tic)
      log.info("TIC")
    case End ⇒
      log.info("going to stop TickActor")
  }

  def tic: Receive = {
    case Tick ⇒
      become(tac)
      log.info("TAC")
    case End ⇒
      log.info("going to stop TickActor")
  }

  override def receive: Receive = {
    case Tick ⇒
      become(tac)
      log.info("TAC")
    case End ⇒
      log.info("going to stop TickActor")
  }
}

object TickActor {

  sealed trait TickActorMessage

  case object Tick extends TickActorMessage

  case object End extends TickActorMessage

}
