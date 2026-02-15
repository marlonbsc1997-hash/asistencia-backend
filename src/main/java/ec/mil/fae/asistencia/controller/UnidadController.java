package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.entity.Unidad;
import ec.mil.fae.asistencia.service.UnidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadController {

    private final UnidadService unidadService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Unidad>>> getAll() {
        List<Unidad> unidades = unidadService.findAll();
        return ResponseEntity.ok(ApiResponse.success(unidades));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Unidad>> getById(@PathVariable Integer id) {
        return unidadService.findById(id)
            .map(unidad -> ResponseEntity.ok(ApiResponse.success(unidad)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Unidad>> create(@RequestBody Unidad unidad) {
        Unidad created = unidadService.create(unidad);
        return ResponseEntity.ok(ApiResponse.success("Unidad creada exitosamente", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Unidad>> update(@PathVariable Integer id, @RequestBody Unidad unidad) {
        Unidad updated = unidadService.update(id, unidad);
        return ResponseEntity.ok(ApiResponse.success("Unidad actualizada exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        unidadService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Unidad eliminada exitosamente", null));
    }
}
