package cl.duoc.guias.consumidor.controller;

import cl.duoc.guias.consumidor.entity.GuiaDespacho;
import cl.duoc.guias.mensajes.GuiaMensaje;
import cl.duoc.guias.consumidor.service.GuiaService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/guias")
public class GuiaController {
    private final GuiaService service;
    public GuiaController(GuiaService service) { this.service = service; }

    @GetMapping("/{numeroGuia}") public GuiaDespacho obtener(@PathVariable String numeroGuia) { return service.buscar(numeroGuia); }
    @GetMapping public List<GuiaDespacho> consultar(@RequestParam String transportista, @RequestParam String fecha) { return service.consultar(transportista, fecha); }
    @PutMapping("/{numeroGuia}") public GuiaDespacho actualizar(@PathVariable String numeroGuia, @RequestBody GuiaMensaje mensaje) { return service.actualizar(numeroGuia, mensaje); }
    @PostMapping("/{numeroGuia}/upload") public ResponseEntity<String> subir(@PathVariable String numeroGuia) { return ResponseEntity.ok("Guía subida a S3: " + service.reSubir(numeroGuia)); }
    @GetMapping("/{numeroGuia}/download") public ResponseEntity<String> descargar(@PathVariable String numeroGuia) {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=guia-" + numeroGuia + ".txt")
                .contentType(MediaType.TEXT_PLAIN).body(service.descargar(numeroGuia));
    }
    @DeleteMapping("/{numeroGuia}") public ResponseEntity<String> eliminar(@PathVariable String numeroGuia) { service.eliminar(numeroGuia); return ResponseEntity.ok("Guía eliminada: " + numeroGuia); }
}
