package service.notification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeadCreadoEvent(
        UUID leadId,
        UUID propiedadId,
        UUID clienteId,
        UUID agenteId,
        UUID visitaId,
        LocalDateTime fechaHoraVisita
) {
}
