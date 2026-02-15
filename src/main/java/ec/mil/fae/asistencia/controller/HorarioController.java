package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.entity.HorarioLaboral;
import ec.mil.fae.asistencia.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horarios")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','EMPLEADO')")
public class HorarioController {

    private final HorarioService horarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HorarioLaboral>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(horarioService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HorarioLaboral>> getById(@PathVariable Integer id) {
        return horarioService.findById(id)
                .map(h -> ResponseEntity.ok(ApiResponse.success(h)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HorarioLaboral>> create(@RequestBody HorarioLaboral horario) {
        return ResponseEntity.ok(ApiResponse.success("Horario creado exitosamente", horarioService.create(horario)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HorarioLaboral>> update(@PathVariable Integer id,
                                                             @RequestBody HorarioLaboral horario) {
        return ResponseEntity.ok(ApiResponse.success("Horario actualizado exitosamente",
                horarioService.update(id, horario)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        horarioService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Horario eliminado exitosamente", null));
    }
}


