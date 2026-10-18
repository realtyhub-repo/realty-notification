package service.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "servicios")
public record ServiciosProperties(String usuariosUrl) {
}
