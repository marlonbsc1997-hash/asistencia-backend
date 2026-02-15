package ec.mil.fae.asistencia.dto.response;

import ec.mil.fae.asistencia.entity.Geocerca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidarUbicacionResponse {
    private boolean dentro;
    private Geocerca geocerca;
    private double distancia;
}
