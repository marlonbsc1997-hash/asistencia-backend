package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.request.UsuarioRequest;
import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.dto.response.UsuarioResponse;
import ec.mil.fae.asistencia.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(usuarioService.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @usuarioSecurity.isSelf(#id, authentication)")
    public ResponseEntity<ApiResponse<UsuarioResponse>> getById(@PathVariable Integer id) {
        return usuarioService.findById(id)
                .map(r -> ResponseEntity.ok(ApiResponse.success(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cedula/{cedula}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<UsuarioResponse>> getByCedula(@PathVariable String cedula) {
        return usuarioService.findByCedula(cedula)
                .map(r -> ResponseEntity.ok(ApiResponse.success(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dependencia/{dependenciaId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> getByDependencia(@PathVariable Integer dependenciaId) {
        return ResponseEntity.ok(ApiResponse.success(usuarioService.findByDependenciaId(dependenciaId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UsuarioResponse>> create(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Usuario creado exitosamente", usuarioService.create(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<UsuarioResponse>> update(@PathVariable Integer id, @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Usuario actualizado exitosamente", usuarioService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        usuarioService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Usuario desactivado", null));
    }
}



