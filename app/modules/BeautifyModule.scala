package modules

import com.google.inject.AbstractModule
import com.kafkaconspiracy.kafka.tools.{HeartbeatProducer, HeartbeatSettings}
import play.api.Environment
import play.api.Configuration

class BeautifyModule(environment: Environment,
                    configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[HeartbeatSettings]) toInstance new HeartbeatSettings(configuration.underlying)
    bind(classOf[HeartbeatProducer]).asEagerSingleton()
  }
}