package icop.parcialpromociontestap.controller;

import icop.parcialpromociontestap.dto.RequestPrestamoDto;
import icop.parcialpromociontestap.dto.ResponsePrestamoDto;
import icop.parcialpromociontestap.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<ResponsePrestamoDto> registrar(@RequestBody RequestPrestamoDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.registrar(request));
    }

    @GetMapping("/socio/{id}")
    public ResponseEntity<List<ResponsePrestamoDto>> listarPorSocio(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.listarPorSocio(id));
    }
}
