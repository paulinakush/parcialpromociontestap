package icop.parcialpromociontestap.service;

import icop.parcialpromociontestap.dto.RequestPrestamoDto;
import icop.parcialpromociontestap.dto.ResponsePrestamoDto;
import icop.parcialpromociontestap.entity.Prestamo;
import icop.parcialpromociontestap.entity.Socio;
import icop.parcialpromociontestap.exception.RecursoNoEncontradoException;
import icop.parcialpromociontestap.exception.ReglaNegocioException;
import icop.parcialpromociontestap.repository.PrestamoRepository;
import icop.parcialpromociontestap.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final SocioRepository socioRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           SocioRepository socioRepository) {
        this.prestamoRepository = prestamoRepository;
        this.socioRepository = socioRepository;
    }

    @Transactional
    public ResponsePrestamoDto registrar(RequestPrestamoDto request) {
        // 1) El socio debe existir (404)
        Socio socio = socioRepository.findById(request.socioId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Socio no encontrado con id: " + request.socioId()));

        // 2) El libro no puede estar actualmente prestado (400)
        if (prestamoRepository.existsByTituloLibroAndDevueltoFalse(request.tituloLibro())) {
            throw new ReglaNegocioException(
                    "El libro " + request.tituloLibro() + " ya se encuentra prestado");
        }

        // 3) Todo préstamo se crea con devuelto = false
        Prestamo prestamo = new Prestamo();
        prestamo.setTituloLibro(request.tituloLibro());
        prestamo.setFechaPrestamo(request.fechaPrestamo());
        prestamo.setDevuelto(false);
        prestamo.setSocio(socio);

        Prestamo guardado = prestamoRepository.save(prestamo);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ResponsePrestamoDto> listarPorSocio(Long socioId) {
        Socio socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Socio no encontrado con id: " + socioId));

        // Se navega la relación del socio (como pide el enunciado)
        return socio.getPrestamos().stream()
                .map(this::toResponse)
                .toList();
    }

    private ResponsePrestamoDto toResponse(Prestamo prestamo) {
        Socio socio = prestamo.getSocio();
        return new ResponsePrestamoDto(
                prestamo.getId(),
                prestamo.getTituloLibro(),
                prestamo.getFechaPrestamo(),
                prestamo.isDevuelto(),
                socio.getNombre() + " " + socio.getApellido());
    }
}