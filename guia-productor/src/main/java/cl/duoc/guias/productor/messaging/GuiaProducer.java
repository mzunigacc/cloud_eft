package cl.duoc.guias.productor.messaging;

import cl.duoc.guias.productor.config.RabbitConfig;
import cl.duoc.guias.mensajes.GuiaMensaje;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class GuiaProducer {
    private final RabbitTemplate rabbitTemplate;

    public GuiaProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviar(GuiaMensaje mensaje) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                mensaje,
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setCorrelationId(mensaje.getNumeroGuia());
                    return message;
                }
        );
    }
}
