package service.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.notification.entity.Notificacion;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface NotificacionRepository extends JpaRepository<Notificacion, UUID> {

    Page<Notificacion> findByUsuarioIdOrderByCreadaEnDesc(UUID usuarioId, Pageable pageable);

    Page<Notificacion> findByUsuarioIdAndLeidaFalseOrderByCreadaEnDesc(UUID usuarioId, Pageable pageable);

    long countByUsuarioIdAndLeidaFalse(UUID usuarioId);

    Optional<Notificacion> findByIdAndUsuarioId(UUID id, UUID usuarioId);

    @Modifying
    @Query(value = """
            INSERT INTO notificacion (id, usuario_id, tipo, titulo, mensaje, referencia_id, clave_dedupe, leida, creada_en)
            VALUES (:id, :usuarioId, :tipo, :titulo, :mensaje, CAST(:referenciaId AS uuid), :claveDedupe, FALSE, :creadaEn)
            ON CONFLICT (usuario_id, clave_dedupe) DO NOTHING
            """, nativeQuery = true)
    int insertarSiNoExiste(@Param("id") UUID id,
                           @Param("usuarioId") UUID usuarioId,
                           @Param("tipo") String tipo,
                           @Param("titulo") String titulo,
                           @Param("mensaje") String mensaje,
                           @Param("referenciaId") UUID referenciaId,
                           @Param("claveDedupe") String claveDedupe,
                           @Param("creadaEn") LocalDateTime creadaEn);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = TRUE, n.leidaEn = :ahora WHERE n.usuarioId = :usuarioId AND n.leida = FALSE")
    int marcarTodasLeidas(@Param("usuarioId") UUID usuarioId, @Param("ahora") LocalDateTime ahora);
}
