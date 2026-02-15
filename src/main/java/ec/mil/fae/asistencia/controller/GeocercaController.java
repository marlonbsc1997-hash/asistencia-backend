package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.request.ValidarUbicacionRequest;
import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.dto.response.ValidarUbicacionResponse;
import ec.mil.fae.asistencia.entity.Geocerca;
import ec.mil.fae.asistencia.service.GeocercaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/geocercas")
@RequiredArgsConstructor
public class GeocercaController {

    private final GeocercaService geocercaService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Geocerca>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(geocercaService.findAll()));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','EMPLEADO')")
    public ResponseEntity<ApiResponse<List<Geocerca>>> getActivas() {
        return ResponseEntity.ok(ApiResponse.success(geocercaService.findByEstadoTrue()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Geocerca>> getById(@PathVariable Integer id) {
        return geocercaService.findById(id)
                .map(g -> ResponseEntity.ok(ApiResponse.success(g)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Geocerca>> create(@RequestBody Geocerca geocerca) {
        Geocerca created = geocercaService.create(geocerca);
        return ResponseEntity.ok(ApiResponse.success("Geocerca creada exitosamente", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Geocerca>> update(@PathVariable Integer id, @RequestBody Geocerca geocerca) {
        Geocerca updated = geocercaService.update(id, geocerca);
        return ResponseEntity.ok(ApiResponse.success("Geocerca actualizada exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        geocercaService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Geocerca eliminada exitosamente", null));
    }

    @PostMapping("/verificar")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','EMPLEADO')")
    public ResponseEntity<ApiResponse<ValidarUbicacionResponse>> verificarUbicacion(
            @RequestBody ValidarUbicacionRequest request) {

        ValidarUbicacionResponse response =
                geocercaService.verificarUbicacion(request.getLatitud(), request.getLongitud());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

