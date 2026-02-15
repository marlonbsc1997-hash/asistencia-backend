package ec.mil.fae.asistencia.service;

import ec.mil.fae.asistencia.dto.NotificacionDTO;

import java.util.List;

public interface NotificacionService {

    // Para el popover (campana)
    List<NotificacionDTO> misNotificaciones(int limit);
    long misNoLeidas();
    void marcarLeida(Integer notId);
    void marcarTodasLeidas();
    void eliminar(Integer notId);

    // Generadores (eventos)
    void generarTardanza(Integer empleadoId, Integer minutos, Integer marcacionId);
    void generarJustificacion(Integer empleadoId, Integer justificacionId);
    void generarAusencia(Integer empleadoId);
}
