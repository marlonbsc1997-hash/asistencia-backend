package ec.mil.fae.asistencia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenSemanal {
    private Double asistenciaPromedio;
    private Double puntualidad;
}
