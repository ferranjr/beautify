package com.kafkaconspiracy.kafka.tools


import akka.actor.ActorSystem
import com.google.inject.{Inject, Singleton}
import kafka.producer.{KeyedMessage, Producer => KafkaProducer, ProducerConfig}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


@Singleton
class HeartbeatProducer @Inject()(system: ActorSystem, heartbeatSettings: HeartbeatSettings) {

  protected val kafkaConfig = new ProducerConfig(KafkaConfig())
  private lazy val producer = new KafkaProducer[String, String](kafkaConfig)

  private val topic = heartbeatSettings.topic
  private val serviceName = heartbeatSettings.serviceName

  val message = Json.toJson(ServiceStatus.create(serviceName, 200)).toString
  system.scheduler.schedule(0 second, 5 seconds) {
    println(s"WOT")
    producer.send(new KeyedMessage[String, String](topic, message))
  }

}
