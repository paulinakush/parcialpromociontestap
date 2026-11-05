package icop.parcialpromociontestap.repository;

import icop.parcialpromociontestap.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    boolean existsByDni(String dni);
}