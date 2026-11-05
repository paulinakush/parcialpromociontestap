package icop.parcialpromociontestap.repository;

import icop.parcialpromociontestap.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    boolean existsByTituloLibroAndDevueltoFalse(String tituloLibro);
}
