package service.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.notification.enums.TipoNotificacion;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoNotificacion tipo;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String mensaje;

    @Column(name = "referencia_id")
    private UUID referenciaId;

    @Column(name = "clave_dedupe", nullable = false, length = 120)
    private String claveDedupe;

    @Column(nullable = false)
    private boolean leida;

    @Column(name = "leida_en")
    private LocalDateTime leidaEn;

    @Column(name = "creada_en", nullable = false)
    private LocalDateTime creadaEn;
}
