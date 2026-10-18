package service.notification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeadOfertaAceptadaEvent(
        UUID leadId,
        UUID propiedadId,
        UUID clienteId,
        UUID agenteId
) {
}
