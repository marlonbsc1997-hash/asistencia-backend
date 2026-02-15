package ec.mil.fae.asistencia.service.impl;

import ec.mil.fae.asistencia.dto.NotificacionDTO;
import ec.mil.fae.asistencia.entity.Notificacion;
import ec.mil.fae.asistencia.entity.Usuario;
import ec.mil.fae.asistencia.repository.NotificacionRepository;
import ec.mil.fae.asistencia.repository.UsuarioRepository;
import ec.mil.fae.asistencia.service.NotificacionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notiRepo;
    private final UsuarioRepository usuarioRepo;

    private Usuario currentUsuario() {
        // ✅ En tu JWT el "username" es el correo (según tu repo findByAuthCorreo)
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioRepo.findByAuthCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para correo: " + correo));
    }

    private List<Usuario> adminsYSupervisores() {
        // ✅ FIX: Role tiene atributo "nombre"
        return usuarioRepo.findByRole_NombreIn(List.of("ADMIN", "SUPERVISOR"));
    }

    // ================= CAMPANA =================

    @Override
    public List<NotificacionDTO> misNotificaciones(int limit) {
        Usuario u = currentUsuario();
        int size = Math.max(1, limit);

        return notiRepo.findByDestinatarioIdOrderByFechaHoraDesc(u.getId(), PageRequest.of(0, size))
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public long misNoLeidas() {
        Usuario u = currentUsuario();
        return notiRepo.countByDestinatarioIdAndLeidoFalse(u.getId());
    }

    @Override
    public void marcarLeida(Integer notId) {
        Usuario u = currentUsuario();

        Notificacion n = notiRepo.findById(notId)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada"));

        if (!n.getDestinatario().getId().equals(u.getId())) {
            throw new SecurityException("No autorizado");
        }

        n.setLeido(true);
        notiRepo.save(n);
    }

    @Override
    public void marcarTodasLeidas() {
        Usuario u = currentUsuario();

        var list = notiRepo.findByDestinatarioIdOrderByFechaHoraDesc(u.getId(), PageRequest.of(0, 500));
        list.forEach(n -> n.setLeido(true));
        notiRepo.saveAll(list);
    }

    @Override
    public void eliminar(Integer notId) {
        Usuario u = currentUsuario();

        Notificacion n = notiRepo.findById(notId)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada"));

        if (!n.getDestinatario().getId().equals(u.getId())) {
            throw new SecurityException("No autorizado");
        }

        notiRepo.delete(n);
    }

    // ================= GENERADORES =================

    @Override
    public void generarTardanza(Integer empleadoId, Integer minutos, Integer marcacionId) {
        Usuario empleado = usuarioRepo.findById(empleadoId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));

        String msg = empleado.getApellidosNombres() + " llegó " + minutos + " minutos tarde.";

        // 1) al empleado
        guardar("tardanza", "Tardanza detectada", msg,
                empleado, empleado, "marcaciones_diarias", marcacionId);

        // 2) a admin/supervisor global
        for (Usuario dest : adminsYSupervisores()) {
            guardar("tardanza", "Tardanza detectada", msg,
                    dest, empleado, "marcaciones_diarias", marcacionId);
        }
    }

    @Override
    public void generarJustificacion(Integer empleadoId, Integer justificacionId) {
        Usuario empleado = usuarioRepo.findById(empleadoId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));

        String msg = empleado.getApellidosNombres() + " solicitó justificación.";

        for (Usuario dest : adminsYSupervisores()) {
            guardar("justificacion", "Nueva justificación", msg,
                    dest, empleado, "justificaciones", justificacionId);
        }
    }

    @Override
    public void generarAusencia(Integer empleadoId) {
        Usuario empleado = usuarioRepo.findById(empleadoId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado"));

        String msg = empleado.getApellidosNombres() + " no ha marcado entrada hoy.";

        for (Usuario dest : adminsYSupervisores()) {
            guardar("ausencia", "Ausencia sin justificar", msg,
                    dest, empleado, "marcaciones_diarias", null);
        }
    }

    // ================= HELPERS =================

    private void guardar(String tipo, String titulo, String mensaje,
                         Usuario destinatario, Usuario origen,
                         String refTabla, Integer refId) {

        Notificacion n = new Notificacion();
        n.setTipo(tipo);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setFechaHora(LocalDateTime.now());
        n.setLeido(false);

        n.setDestinatario(destinatario);
        n.setOrigen(origen);

        n.setRefTabla(refTabla);
        n.setRefId(refId);

        notiRepo.save(n);
    }

    private NotificacionDTO toDto(Notificacion n) {
        NotificacionDTO dto = new NotificacionDTO();
        dto.setId(n.getId());
        dto.setType(n.getTipo());
        dto.setTitle(n.getTitulo());
        dto.setMessage(n.getMensaje());
        dto.setTimestamp(n.getFechaHora());
        dto.setRead(Boolean.TRUE.equals(n.getLeido()));

        if (n.getOrigen() != null) {
            dto.setUserId(n.getOrigen().getId());
            dto.setUserName(n.getOrigen().getApellidosNombres());
        }
        return dto;
    }
}

