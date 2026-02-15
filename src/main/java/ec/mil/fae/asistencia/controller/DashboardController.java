package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.response.ApiResponse;
import ec.mil.fae.asistencia.dto.response.ActividadReciente;
import ec.mil.fae.asistencia.dto.response.DashboardEstadisticas;
import ec.mil.fae.asistencia.dto.response.DashboardResumenSemanal;
import ec.mil.fae.asistencia.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/estadisticas")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<DashboardEstadisticas>> getEstadisticas(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String periodo) {
        DashboardEstadisticas estadisticas = dashboardService.getEstadisticas(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(estadisticas));
    }

    @GetMapping("/resumen-semanal")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<DashboardResumenSemanal>> getResumenSemanal(
            @AuthenticationPrincipal UserDetails userDetails) {
        DashboardResumenSemanal resumen = dashboardService.getResumenSemanal(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(resumen));
    }

    @GetMapping("/actividad-reciente")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ActividadReciente>>> getActividadReciente(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false, defaultValue = "10") Integer limite) {
        List<ActividadReciente> actividad = dashboardService.getActividadReciente(userDetails.getUsername(), limite);
        return ResponseEntity.ok(ApiResponse.success(actividad));
    }
}


