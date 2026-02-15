// ===============================
// 5) CONTROLLER: DiaFestivoController.java
// ===============================
package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.entity.DiaFestivo;
import ec.mil.fae.asistencia.service.DiaFestivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dias-festivos")
@RequiredArgsConstructor
public class DiaFestivoController {

    private final DiaFestivoService diaFestivoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiaFestivo>>> getAll() {
        List<DiaFestivo> list = diaFestivoService.findAll();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiaFestivo>> getById(@PathVariable Integer id) {
        return diaFestivoService.findById(id)
            .map(item -> ResponseEntity.ok(ApiResponse.success(item)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiaFestivo>> create(@RequestBody DiaFestivo diaFestivo) {
        DiaFestivo created = diaFestivoService.create(diaFestivo);
        return ResponseEntity.ok(ApiResponse.success("Día festivo creado exitosamente", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DiaFestivo>> update(
        @PathVariable Integer id,
        @RequestBody DiaFestivo diaFestivo
    ) {
        DiaFestivo updated = diaFestivoService.update(id, diaFestivo);
        return ResponseEntity.ok(ApiResponse.success("Día festivo actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        diaFestivoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Día festivo eliminado exitosamente", null));
    }
}
