package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.entity.Role;
import ec.mil.fae.asistencia.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // ✅ ADMIN y SUPERVISOR pueden leer roles
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Role>>> getAll() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(ApiResponse.success(roles));
    }

    // ✅ ADMIN y SUPERVISOR pueden ver un rol
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<Role>> getById(@PathVariable Integer id) {
        return roleService.findById(id)
                .map(role -> ResponseEntity.ok(ApiResponse.success(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔒 Solo ADMIN puede crear roles
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> create(@RequestBody Role role) {
        Role created = roleService.create(role);
        return ResponseEntity.ok(ApiResponse.success("Rol creado exitosamente", created));
    }

    // 🔒 Solo ADMIN puede actualizar roles
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> update(@PathVariable Integer id, @RequestBody Role rol) {
        Role updated = roleService.update(id, rol);
        return ResponseEntity.ok(ApiResponse.success("Rol actualizado exitosamente", updated));
    }

    // 🔒 Solo ADMIN puede eliminar roles
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        roleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Rol eliminado exitosamente", null));
    }
}

