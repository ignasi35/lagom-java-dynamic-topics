package com.example.helloworld.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface HelloWorldService extends Service {
    ServiceCall<NotUsed, String> hello(String id);
    ServiceCall<GreetingMessage, Done> useGreeting(String id);
    Topic<HelloWorldEvent> helloEvents();

    default String getTopicName() {
        return "my-topic";
    }

    @Override
    default Descriptor descriptor() {
        return named("helloWorld")
                .withCalls(
                        pathCall("/api/hello/:id", this::hello),
                        pathCall("/api/hello/:id", this::useGreeting)
                )
                .withTopics(
                        topic(getTopicName(), this::helloEvents)
                                .withProperty(KafkaProperties.partitionKeyStrategy(), HelloWorldEvent::getName)
                )
                .withAutoAcl(true);
    }
}
