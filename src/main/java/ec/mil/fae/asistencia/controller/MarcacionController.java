package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.request.MarcacionRequest;
import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.entity.Marcacion;
import ec.mil.fae.asistencia.service.MarcacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/marcaciones")
@RequiredArgsConstructor
public class MarcacionController {

    private final MarcacionService marcacionService;

    @PostMapping("/marcar")
    public ResponseEntity<ApiResponse<Marcacion>> marcar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MarcacionRequest request,
            HttpServletRequest httpRequest
    ) {
        String identificador = userDetails.getUsername();
        String ip = getClientIp(httpRequest);

        Marcacion marcacion = marcacionService.registrarMarcacion(
                identificador,
                request.getTipoMarcacion(),   // ✅ enum
                request.getLatitud(),
                request.getLongitud(),
                ip
        );

        return ResponseEntity.ok(ApiResponse.success("Marcación registrada exitosamente", marcacion));
    }

    @GetMapping("/mis-marcaciones")
    public ResponseEntity<ApiResponse<List<Marcacion>>> getMisMarcaciones(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        LocalDate[] rango = normalizarRango(fechaInicio, fechaFin);
        List<Marcacion> marcaciones = marcacionService.findMisMarcaciones(
                userDetails.getUsername(), rango[0], rango[1]
        );
        return ResponseEntity.ok(ApiResponse.success(marcaciones));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Marcacion>>> getByUsuario(
            @PathVariable Integer usuarioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        LocalDate[] rango = normalizarRango(fechaInicio, fechaFin);
        List<Marcacion> marcaciones = marcacionService.findByUsuarioIdAndFechaRange(usuarioId, rango[0], rango[1]);
        return ResponseEntity.ok(ApiResponse.success(marcaciones));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<Marcacion>>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        LocalDate[] rango = normalizarRango(fechaInicio, fechaFin);
        List<Marcacion> marcaciones = marcacionService.findByFechaRange(rango[0], rango[1]);
        return ResponseEntity.ok(ApiResponse.success(marcaciones));
    }

    private LocalDate[] normalizarRango(LocalDate inicio, LocalDate fin) {
        if (inicio == null && fin == null) {
            LocalDate hoy = LocalDate.now();
            return new LocalDate[]{hoy, hoy};
        }
        if (inicio == null) inicio = fin;
        if (fin == null) fin = inicio;
        return new LocalDate[]{inicio, fin};
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf != null && !xf.isBlank()) return xf.split(",")[0].trim();
        return request.getRemoteAddr();
    }
}

