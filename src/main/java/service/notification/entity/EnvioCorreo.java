package service.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.notification.enums.EstadoEnvioCorreo;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "envio_correo")
public class EnvioCorreo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "notificacion_id", nullable = false)
    private UUID notificacionId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(nullable = false, length = 200)
    private String asunto;

    @Column(nullable = false, length = 500)
    private String cuerpo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoEnvioCorreo estado;

    @Column(nullable = false)
    private int intentos;

    @Column(name = "ultimo_error", length = 500)
    private String ultimoError;

    @Column(name = "proximo_intento_en", nullable = false)
    private LocalDateTime proximoIntentoEn;

    @Column(name = "enviado_en")
    private LocalDateTime enviadoEn;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    void alCrear() {
        if (creadoEn == null) {
            creadoEn = LocalDateTime.now();
        }
        if (proximoIntentoEn == null) {
            proximoIntentoEn = creadoEn;
        }
    }
}
