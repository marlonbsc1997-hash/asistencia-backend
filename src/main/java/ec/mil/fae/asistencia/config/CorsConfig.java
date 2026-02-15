package ec.mil.fae.asistencia.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

  private final CorsProperties corsProperties;

  public CorsConfig(CorsProperties corsProperties) {
    this.corsProperties = corsProperties;
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    // Métodos y headers
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization"));

    /**
     * Si usas JWT por header (Authorization), puedes dejar esto en true o false.
     * - true: si en algún momento usas cookies (session) o necesitas credentials.
     * - false: si vas a permitir "*" y evitar conflictos de CORS con credenciales.
     */
    config.setAllowCredentials(true);

    // 1) Modo "abrir todo" para DEV (cuando cambias de red a cada rato)
    if (corsProperties.isAllowAnyOrigin()) {
      // Para no chocar con allowCredentials=true, lo más seguro es ponerlo en false en este modo
      config.setAllowCredentials(false);
      config.setAllowedOriginPatterns(List.of("*"));
    }
    // 2) Patrones (recomendado para DHCP)
    else if (!CollectionUtils.isEmpty(corsProperties.getAllowedOriginPatterns())) {
      config.setAllowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
    }
    // 3) Orígenes exactos (fallback)
    else if (!CollectionUtils.isEmpty(corsProperties.getAllowedOrigins())) {
      config.setAllowedOrigins(corsProperties.getAllowedOrigins());
    }
    // 4) Último fallback (no te deja botado en local)
    else {
      config.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
    }

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}


