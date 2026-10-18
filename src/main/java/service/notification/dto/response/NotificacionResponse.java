package service.notification.dto.response;

import service.notification.entity.Notificacion;
import service.notification.enums.TipoNotificacion;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacionResponse(
        UUID id,
        TipoNotificacion tipo,
        String titulo,
        String mensaje,
        UUID referenciaId,
        boolean leida,
        LocalDateTime leidaEn,
        LocalDateTime creadaEn
) {
    public static NotificacionResponse from(Notificacion notificacion) {
        return new NotificacionResponse(
                notificacion.getId(),
                notificacion.getTipo(),
                notificacion.getTitulo(),
                notificacion.getMensaje(),
                notificacion.getReferenciaId(),
                notificacion.isLeida(),
                notificacion.getLeidaEn(),
                notificacion.getCreadaEn());
    }
}
