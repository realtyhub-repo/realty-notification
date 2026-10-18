package service.notification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CommissionStatusEvent(
        UUID comisionId,
        UUID contratoId,
        UUID agenteId,
        UUID gerenteOficinaId,
        String estado,
        BigDecimal montoTotal,
        LocalDateTime ocurridoEn
) {
}
