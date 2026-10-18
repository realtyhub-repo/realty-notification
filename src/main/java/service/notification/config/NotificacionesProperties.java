package service.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notificaciones")
public record NotificacionesProperties(Correo correo) {

    public record Correo(boolean habilitado, String remitente, int maxIntentos, int lote) {
    }
}
