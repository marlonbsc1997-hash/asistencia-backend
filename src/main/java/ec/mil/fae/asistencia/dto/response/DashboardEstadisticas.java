package ec.mil.fae.asistencia.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class DashboardEstadisticas {
    private long totalEmpleados;
    private long presentesHoy;
    private long tardanzasMes;
    private long ausenciasMes;
    private long justificacionesPendientes;
    private double porcentajeAsistencia;
    private List<EstadisticaDependencia> estadisticasPorDependencia;
    private List<TendenciaMensual> tendenciaMensual;

    @Data
    public static class EstadisticaDependencia {
        private String dependencia;
        private long puntuales;
        private long tardanzas;
        private long ausencias;
    }

    @Data
    public static class TendenciaMensual {
        private String mes;
        private long puntuales;
        private long tardanzas;
        private long ausencias;
        private double puntualidad;
    }
}


