package cl.duoc.guias.consumidor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "guia.exchange";
    public static final String QUEUE = "guia.procesamiento";
    public static final String ROUTING_KEY = "guia.crear";
    public static final String DLX = "guia.dlx";
    public static final String DLQ = "guia.dlq";
    public static final String DLQ_ROUTING_KEY = "guia.error";

    @Bean DirectExchange guiaExchange() { return new DirectExchange(EXCHANGE, true, false); }
    @Bean DirectExchange deadLetterExchange() { return new DirectExchange(DLX, true, false); }
    @Bean Queue guiaQueue() { return QueueBuilder.durable(QUEUE).deadLetterExchange(DLX).deadLetterRoutingKey(DLQ_ROUTING_KEY).build(); }
    @Bean Queue deadLetterQueue() { return QueueBuilder.durable(DLQ).build(); }
    @Bean Binding guiaBinding(Queue guiaQueue, DirectExchange guiaExchange) { return BindingBuilder.bind(guiaQueue).to(guiaExchange).with(ROUTING_KEY); }
    @Bean Binding dlqBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) { return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(DLQ_ROUTING_KEY); }
    @Bean MessageConverter jsonMessageConverter() { return new Jackson2JsonMessageConverter(); }
    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
