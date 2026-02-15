package ec.mil.fae.asistencia.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

  /**
   * Para permitir orígenes exactos (opcional).
   * Ej: http://localhost:5173
   */
  private List<String> allowedOrigins;

  /**
   * Para permitir patrones con comodines (RECOMENDADO para DHCP).
   * Ej: http://*:*, https://*
   */
  private List<String> allowedOriginPatterns;

  /**
   * Si quieres “modo dev” para abrir todo (cualquier origen).
   * Útil cuando trabajas desde redes diferentes.
   */
  private boolean allowAnyOrigin = false;
}
