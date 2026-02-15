package ec.mil.fae.asistencia.mapper;

import ec.mil.fae.asistencia.dto.response.JustificacionResponse;
import ec.mil.fae.asistencia.entity.Justificacion;

public class JustificacionMapper {

    private JustificacionMapper() {}

    public static JustificacionResponse toResponse(Justificacion j) {
        return JustificacionResponse.builder()
                .id(j.getId())

                .usuarioId(j.getUsuario() != null ? j.getUsuario().getId() : null)
                .usuarioNombres(j.getUsuario() != null ? j.getUsuario().getApellidosNombres() : null)
                .usuarioCedula(j.getUsuario() != null ? j.getUsuario().getCedula() : null)

                .marcacionId(j.getMarcacion() != null ? j.getMarcacion().getId() : null)

                .fecha(j.getFecha())
                .motivo(j.getMotivo())
                .estado(j.getEstado())

                .documento(j.getDocumento())

                .aprobadoPorId(j.getAprobadoPor() != null ? j.getAprobadoPor().getId() : null)
                .aprobadoPorNombres(j.getAprobadoPor() != null ? j.getAprobadoPor().getApellidosNombres() : null)
                .fechaAprobacion(j.getFechaAprobacion())

                .fechaCreacion(j.getFechaCreacion())
                .build();
    }
}


