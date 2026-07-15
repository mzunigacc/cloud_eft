package cl.duoc.guias.consumidor.repository;
import cl.duoc.guias.consumidor.entity.GuiaDespacho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface GuiaDespachoRepository extends JpaRepository<GuiaDespacho, Long> {
    Optional<GuiaDespacho> findByNumeroGuia(String numeroGuia);
    List<GuiaDespacho> findByTransportistaIgnoreCaseAndFecha(String transportista, String fecha);
}
