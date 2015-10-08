package com.kafkaconspiracy.kafka.tools

import com.typesafe.config.Config

class HeartbeatSettings(config: Config) {

  val topic = config.getString("heartbeat.topic")
  val serviceName = config.getString("heartbeat.service.name")

}