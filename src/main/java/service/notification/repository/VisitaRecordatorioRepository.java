package service.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service.notification.entity.VisitaRecordatorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VisitaRecordatorioRepository extends JpaRepository<VisitaRecordatorio, UUID> {

    @Query("""
            SELECT v FROM VisitaRecordatorio v
            WHERE v.estado = service.notification.enums.EstadoVisitaRecordatorio.PROGRAMADA
              AND v.recordatorio24hEn IS NULL
              AND v.fechaHora > :ahora
              AND v.fechaHora <= :limite
            """)
    List<VisitaRecordatorio> pendientesDe24Horas(@Param("ahora") LocalDateTime ahora,
                                                 @Param("limite") LocalDateTime limite);

    @Query("""
            SELECT v FROM VisitaRecordatorio v
            WHERE v.estado = service.notification.enums.EstadoVisitaRecordatorio.PROGRAMADA
              AND v.recordatorio1hEn IS NULL
              AND v.fechaHora > :ahora
              AND v.fechaHora <= :limite
            """)
    List<VisitaRecordatorio> pendientesDeUnaHora(@Param("ahora") LocalDateTime ahora,
                                                 @Param("limite") LocalDateTime limite);
}
