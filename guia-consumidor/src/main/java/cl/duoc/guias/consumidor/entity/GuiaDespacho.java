package cl.duoc.guias.consumidor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guias_despacho")
public class GuiaDespacho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="numero_guia", nullable=false, unique=true) private String numeroGuia;
    @Column(nullable=false) private String transportista;
    @Column(nullable=false) private String fecha;
    private String origen;
    private String destino;
    private String destinatario;
    @Column(name="descripcion_pedido", length=1000) private String descripcionPedido;
    @Column(name="cantidad_bultos") private Integer cantidadBultos;
    @Column(name="s3_key", length=1000) private String s3Key;
    @Column(name="procesada_en") private LocalDateTime procesadaEn;
    public Long getId() { return id; }
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
    public String getS3Key() { return s3Key; }
    public void setS3Key(String s3Key) { this.s3Key = s3Key; }
    public LocalDateTime getProcesadaEn() { return procesadaEn; }
    public void setProcesadaEn(LocalDateTime procesadaEn) { this.procesadaEn = procesadaEn; }
}
