package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.request.JustificacionCreateRequest;
import ec.mil.fae.asistencia.dto.request.JustificacionRechazoRequest;
import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.dto.response.JustificacionResponse;
import ec.mil.fae.asistencia.service.JustificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/justificaciones")
@RequiredArgsConstructor
public class JustificacionController {

    private final JustificacionService justificacionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<JustificacionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(justificacionService.findAll()));
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<JustificacionResponse>>> getPendientes() {
        return ResponseEntity.ok(ApiResponse.success(justificacionService.findPendientes()));
    }

    @GetMapping("/mis-justificaciones")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','EMPLEADO')")
    public ResponseEntity<ApiResponse<List<JustificacionResponse>>> getMisJustificaciones(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(justificacionService.findMine(userDetails.getUsername())));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','EMPLEADO')")
    public ResponseEntity<ApiResponse<JustificacionResponse>> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody JustificacionCreateRequest req) {

        JustificacionResponse created = justificacionService.create(userDetails.getUsername(), req);
        return ResponseEntity.ok(ApiResponse.success("Justificación creada exitosamente", created));
    }

    @PostMapping(value = "/con-documento", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','EMPLEADO')")
    public ResponseEntity<ApiResponse<JustificacionResponse>> createWithDocument(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("data") JustificacionCreateRequest req,
            @RequestPart(value = "documento", required = false) MultipartFile documento) {

        JustificacionResponse created =
                justificacionService.createWithDocument(userDetails.getUsername(), req, documento);

        return ResponseEntity.ok(ApiResponse.success("Justificación creada exitosamente", created));
    }

    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<JustificacionResponse>> aprobar(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {

        JustificacionResponse updated = justificacionService.aprobar(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Justificación aprobada exitosamente", updated));
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<JustificacionResponse>> rechazar(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody(required = false) JustificacionRechazoRequest body) {

        String motivo = (body != null) ? body.getMotivo() : null;
        JustificacionResponse updated = justificacionService.rechazar(id, userDetails.getUsername(), motivo);
        return ResponseEntity.ok(ApiResponse.success("Justificación rechazada", updated));
    }

    @GetMapping("/{id}/documento")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<Resource> downloadDocumento(@PathVariable Integer id) {
        return justificacionService.downloadDocumento(id);
    }
}


