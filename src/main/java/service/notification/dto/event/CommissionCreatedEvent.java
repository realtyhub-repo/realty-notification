package service.notification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommissionCreatedEvent(
        UUID comisionId,
        UUID contratoId,
        UUID propiedadId,
        UUID agenteId,
        UUID gerenteOficinaId,
        BigDecimal montoTotal,
        LocalDateTime creadaEn
) {
}
