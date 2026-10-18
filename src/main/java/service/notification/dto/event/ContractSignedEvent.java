package service.notification.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ContractSignedEvent(
        UUID contratoId,
        UUID leadId,
        UUID propiedadId,
        UUID clienteId,
        UUID agenteId,
        String tipo,
        BigDecimal precioAcordado,
        LocalDateTime firmadoEn
) {
}
