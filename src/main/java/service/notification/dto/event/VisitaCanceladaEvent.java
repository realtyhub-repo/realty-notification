package service.notification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VisitaCanceladaEvent(
        UUID visitaId,
        UUID leadId,
        UUID propiedadId,
        UUID clienteId,
        UUID agenteId,
        LocalDateTime fechaHora,
        LocalDateTime fechaHoraFin,
        UUID canceladaPor,
        String motivo
) {
}
