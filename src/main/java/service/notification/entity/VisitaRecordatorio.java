package service.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.notification.enums.EstadoVisitaRecordatorio;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "visita_recordatorio")
public class VisitaRecordatorio {

    @Id
    @Column(name = "visita_id")
    private UUID visitaId;

    @Column(name = "lead_id", nullable = false)
    private UUID leadId;

    @Column(name = "propiedad_id", nullable = false)
    private UUID propiedadId;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "agente_id", nullable = false)
    private UUID agenteId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoVisitaRecordatorio estado;

    @Column(name = "recordatorio_24h_en")
    private LocalDateTime recordatorio24hEn;

    @Column(name = "recordatorio_1h_en")
    private LocalDateTime recordatorio1hEn;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @PrePersist
    void alCrear() {
        LocalDateTime ahora = LocalDateTime.now();
        creadoEn = ahora;
        actualizadoEn = ahora;
    }

    @PreUpdate
    void alActualizar() {
        actualizadoEn = LocalDateTime.now();
    }
}
