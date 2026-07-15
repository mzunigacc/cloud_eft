package cl.duoc.guias.productor.controller;

import cl.duoc.guias.productor.messaging.GuiaProducer;
import cl.duoc.guias.mensajes.GuiaMensaje;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/guias")
public class GuiaProducerController {
    private final GuiaProducer producer;

    public GuiaProducerController(GuiaProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> crear(@Valid @RequestBody GuiaMensaje mensaje) {
        producer.enviar(mensaje);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "estado", "ENCOLADA",
                "mensaje", "Guía enviada a RabbitMQ",
                "numeroGuia", mensaje.getNumeroGuia()
        ));
    }
}
