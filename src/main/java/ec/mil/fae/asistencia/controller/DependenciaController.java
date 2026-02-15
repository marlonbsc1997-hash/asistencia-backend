package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.request.DependenciaRequest;
import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.entity.Dependencia;
import ec.mil.fae.asistencia.service.DependenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dependencias")
@RequiredArgsConstructor
public class DependenciaController {

    private final DependenciaService dependenciaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Dependencia>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(dependenciaService.findAll()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Dependencia>> create(
            @RequestBody DependenciaRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Dependencia creada exitosamente",
                        dependenciaService.create(request)
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Dependencia>> update(
            @PathVariable Integer id,
            @RequestBody DependenciaRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Dependencia actualizada exitosamente",
                        dependenciaService.update(id, request)
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        dependenciaService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Dependencia eliminada", null));
    }
}


