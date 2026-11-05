package icop.parcialpromociontestap.service;

import icop.parcialpromociontestap.dto.RequestSocioDto;
import icop.parcialpromociontestap.dto.ResponseSocioDto;
import icop.parcialpromociontestap.entity.Socio;
import icop.parcialpromociontestap.exception.RecursoNoEncontradoException;
import icop.parcialpromociontestap.exception.ReglaNegocioException;
import icop.parcialpromociontestap.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @Transactional(readOnly = true)
    public List<ResponseSocioDto> listar() {
        return socioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseSocioDto buscarPorId(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Socio no encontrado con id: " + id));
        return toResponse(socio);
    }

    @Transactional
    public ResponseSocioDto crear(RequestSocioDto request) {
        if (socioRepository.existsByDni(request.dni())) {
            throw new ReglaNegocioException(
                    "Ya existe un socio con el DNI: " + request.dni());
        }
        Socio socio = new Socio(request.nombre(), request.apellido(), request.dni());
        Socio guardado = socioRepository.save(socio);
        return toResponse(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Socio no encontrado con id: " + id));

        if (!socio.getPrestamos().isEmpty()) {
            throw new ReglaNegocioException(
                    "No se puede eliminar el socio con id: " + id
                            + " porque tiene préstamos asociados");
        }
        socioRepository.delete(socio);
    }

    private ResponseSocioDto toResponse(Socio socio) {
        return new ResponseSocioDto(
                socio.getNombre() + " " + socio.getApellido(),
                socio.getDni());
    }
}