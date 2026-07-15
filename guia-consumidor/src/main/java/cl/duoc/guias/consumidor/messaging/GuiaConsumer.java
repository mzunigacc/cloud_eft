package cl.duoc.guias.consumidor.messaging;

import cl.duoc.guias.consumidor.config.RabbitConfig;
import cl.duoc.guias.mensajes.GuiaMensaje;
import cl.duoc.guias.consumidor.service.GuiaService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GuiaConsumer {
    private final GuiaService guiaService;
    public GuiaConsumer(GuiaService guiaService) { this.guiaService = guiaService; }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consumir(GuiaMensaje mensaje) {
        try {
            String descripcion = mensaje.getDescripcionPedido();
            if (descripcion != null && descripcion.trim().equalsIgnoreCase("error")) {
                throw new IllegalStateException("Error forzado para demostrar la DLQ");
            }
            guiaService.procesar(mensaje);
            System.out.println("Guía procesada correctamente: " + mensaje.getNumeroGuia());
        } catch (Exception ex) {
            System.err.println("Guía rechazada y enviada a DLQ: " + mensaje.getNumeroGuia() + " - " + ex.getMessage());
            throw new AmqpRejectAndDontRequeueException("Error procesando guía", ex);
        }
    }
}
