package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.entity.Funcion;
import ec.mil.fae.asistencia.service.FuncionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funciones")
@RequiredArgsConstructor
public class FuncionController {

    private final FuncionService funcionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Funcion>>> getAll() {
        List<Funcion> funciones = funcionService.findAll();
        return ResponseEntity.ok(ApiResponse.success(funciones));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Funcion>> getById(@PathVariable Integer id) {
        return funcionService.findById(id)
            .map(funcion -> ResponseEntity.ok(ApiResponse.success(funcion)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Funcion>> create(@RequestBody Funcion funcion) {
        Funcion created = funcionService.create(funcion);
        return ResponseEntity.ok(ApiResponse.success("Función creada exitosamente", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Funcion>> update(@PathVariable Integer id, @RequestBody Funcion funcion) {
        Funcion updated = funcionService.update(id, funcion);
        return ResponseEntity.ok(ApiResponse.success("Función actualizada exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        funcionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Función eliminada exitosamente", null));
    }
}
