package ec.mil.fae.asistencia.controller;

import ec.mil.fae.asistencia.dto.NotificacionDTO;
import ec.mil.fae.asistencia.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService service;

    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> listar(@RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(service.misNotificaciones(limit));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> unreadCount() {
        return ResponseEntity.ok(service.misNoLeidas());
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> marcarLeida(@PathVariable Integer id) {
        service.marcarLeida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> marcarTodas() {
        service.marcarTodasLeidas();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
