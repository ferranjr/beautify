package com.kafkaconspiracy.kafka.tools

import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.{Inject, Singleton}

import akka.pattern.ask
import akka.actor.{Actor, Props, ActorSystem}
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{Format, JsPath, Reads, Json}
import play.api.libs.functional.syntax._

case class ServiceStatus(id: String, serviceName: String, status: Int) {
  import ServiceStatus._

  val lastUpdate = System.currentTimeMillis()

  def toJson = Json.toJson(this)

  override def toString() = s"(serviceName = $serviceName, status = $status, lastUpdate = $lastUpdate)"

}

object ServiceStatus {
  implicit val reader:Format[ServiceStatus] = Json.format[ServiceStatus]

  def create(serviceName: String, status: Int) = ServiceStatus(java.util.UUID.randomUUID().toString,serviceName,status)

}