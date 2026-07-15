package cl.duoc.guias.consumidor.service;

import cl.duoc.guias.consumidor.entity.GuiaDespacho;
import cl.duoc.guias.mensajes.GuiaMensaje;
import cl.duoc.guias.consumidor.repository.GuiaDespachoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuiaService {
    private final GuiaDespachoRepository repository;
    private final GuiaArchivoService archivoService;
    private final S3Service s3Service;
    public GuiaService(GuiaDespachoRepository repository, GuiaArchivoService archivoService, S3Service s3Service) {
        this.repository = repository; this.archivoService = archivoService; this.s3Service = s3Service;
    }
    public GuiaDespacho procesar(GuiaMensaje m) {
        GuiaDespacho g = repository.findByNumeroGuia(m.getNumeroGuia()).orElseGet(GuiaDespacho::new);
        copiar(m, g);
        String key = archivoService.key(g);
        s3Service.subir(key, archivoService.contenido(g));
        g.setS3Key(key);
        g.setProcesadaEn(LocalDateTime.now());
        return repository.save(g);
    }
    public GuiaDespacho buscar(String numero) { return repository.findByNumeroGuia(numero).orElseThrow(() -> new IllegalArgumentException("Guía no encontrada: " + numero)); }
    public List<GuiaDespacho> consultar(String transportista, String fecha) { return repository.findByTransportistaIgnoreCaseAndFecha(transportista, fecha); }
    public String descargar(String numero) { GuiaDespacho g = buscar(numero); return s3Service.descargar(g.getS3Key()); }
    public GuiaDespacho actualizar(String numero, GuiaMensaje m) {
        GuiaDespacho g = buscar(numero); m.setNumeroGuia(numero); copiar(m, g);
        String oldKey = g.getS3Key(); String newKey = archivoService.key(g);
        s3Service.subir(newKey, archivoService.contenido(g));
        if (oldKey != null && !oldKey.equals(newKey)) s3Service.eliminar(oldKey);
        g.setS3Key(newKey); g.setProcesadaEn(LocalDateTime.now()); return repository.save(g);
    }
    public String reSubir(String numero) { GuiaDespacho g = buscar(numero); String key = archivoService.key(g); s3Service.subir(key, archivoService.contenido(g)); g.setS3Key(key); repository.save(g); return key; }
    public void eliminar(String numero) { GuiaDespacho g = buscar(numero); if (g.getS3Key() != null) s3Service.eliminar(g.getS3Key()); repository.delete(g); }
    private void copiar(GuiaMensaje m, GuiaDespacho g) {
        g.setNumeroGuia(m.getNumeroGuia()); g.setTransportista(m.getTransportista()); g.setFecha(m.getFecha());
        g.setOrigen(m.getOrigen()); g.setDestino(m.getDestino()); g.setDestinatario(m.getDestinatario());
        g.setDescripcionPedido(m.getDescripcionPedido()); g.setCantidadBultos(m.getCantidadBultos());
    }
}
