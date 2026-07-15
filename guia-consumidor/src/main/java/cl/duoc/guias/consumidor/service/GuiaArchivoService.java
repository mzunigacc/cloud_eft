package cl.duoc.guias.consumidor.service;
import cl.duoc.guias.consumidor.entity.GuiaDespacho;
import org.springframework.stereotype.Service;
@Service
public class GuiaArchivoService {
    public String contenido(GuiaDespacho g) {
        return """
                GUÍA DE DESPACHO

                Número guía: %s
                Transportista: %s
                Fecha: %s
                Origen: %s
                Destino: %s
                Destinatario: %s
                Pedido: %s
                Cantidad de bultos: %s
                """.formatted(g.getNumeroGuia(), g.getTransportista(), g.getFecha(), g.getOrigen(), g.getDestino(),
                g.getDestinatario(), g.getDescripcionPedido(), g.getCantidadBultos());
    }
    public String key(GuiaDespacho g) {
        return normalizar(g.getTransportista()) + "/" + g.getFecha() + "/guia-" + g.getNumeroGuia() + ".txt";
    }
    private String normalizar(String value) { return value.trim().toLowerCase().replaceAll("[^a-z0-9-]", "-"); }
}
