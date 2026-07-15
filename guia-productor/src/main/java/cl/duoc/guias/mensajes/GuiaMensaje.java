package cl.duoc.guias.mensajes;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public class GuiaMensaje implements Serializable {
    @NotBlank private String numeroGuia;
    @NotBlank private String transportista;
    @NotBlank private String fecha;
    private String origen;
    private String destino;
    private String destinatario;
    private String descripcionPedido;
    @Min(1) private Integer cantidadBultos;

    public GuiaMensaje() {}
    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public String getDescripcionPedido() { return descripcionPedido; }
    public void setDescripcionPedido(String descripcionPedido) { this.descripcionPedido = descripcionPedido; }
    public Integer getCantidadBultos() { return cantidadBultos; }
    public void setCantidadBultos(Integer cantidadBultos) { this.cantidadBultos = cantidadBultos; }
}
