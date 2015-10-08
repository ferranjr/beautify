package com.kafkaconspiracy.kafka.tools

import java.util.Properties

import com.typesafe.config.ConfigFactory
import kafka.consumer.ConsumerConfig
import kafka.consumer.{ Consumer => KafkaConsumer }


abstract class Consumer(topics: List[String]) {

  protected val kafkaConfig = KafkaConfig()
  protected val config = new ConsumerConfig(kafkaConfig)

  def read(): Iterable[String]
}

case class SingleTopicConsumer(topic: String) extends Consumer(List(topic)) {
  private lazy val consumer = KafkaConsumer.create(config)
  val threadNum = 1

  private lazy val consumerMap = consumer.createMessageStreams(Map(topic -> threadNum))
  val stream = consumerMap.getOrElse(topic, List()).head

  override def read(): Stream[String] = Stream.cons(new String(stream.head.message()), read())
}

trait KafkaConfig extends Properties {

  import KafkaConfig._

  private val consumerPrefixWithDot = consumerPrefix + "."
  private val producerPrefixWithDot = producerPrefix + "."
  private val allKeys = Seq(groupId, zookeeperConnect, brokers, serializer, partitioner, requiredAcks)

  lazy val typesafeConfig = ConfigFactory.load()

  allKeys.map { key =>
    if (typesafeConfig.hasPath(key))
      put(key.replace(consumerPrefixWithDot, "").replace(producerPrefixWithDot, ""), typesafeConfig.getString(key))
  }

  def getCustomString(key: String) = typesafeConfig.getString(key)
  def getCustomInt(key: String) = typesafeConfig.getInt(key)
}

object KafkaConfig {

  val consumerPrefix = "consumer"
  val producerPrefix = "producer"

  //Consumer keys
  val groupId = s"$consumerPrefix.group.id"
  val zookeeperConnect = s"$consumerPrefix.zookeeper.connect"

  //example.producer.Producer keys
  val brokers = s"$producerPrefix.metadata.broker.list"
  val serializer = s"$producerPrefix.serializer.class"
  val partitioner = s"$producerPrefix.partitioner.class"
  val requiredAcks = s"$producerPrefix.request.required.acks"

  def apply() = new KafkaConfig {}
}