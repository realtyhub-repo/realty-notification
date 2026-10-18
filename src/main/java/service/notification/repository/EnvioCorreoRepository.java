package service.notification.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.notification.entity.EnvioCorreo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EnvioCorreoRepository extends JpaRepository<EnvioCorreo, UUID> {

    @Query("""
            SELECT e FROM EnvioCorreo e
            WHERE e.estado = service.notification.enums.EstadoEnvioCorreo.PENDIENTE
              AND e.proximoIntentoEn <= :ahora
            ORDER BY e.proximoIntentoEn
            """)
    List<EnvioCorreo> buscarPendientes(@Param("ahora") LocalDateTime ahora, Pageable pageable);
}
