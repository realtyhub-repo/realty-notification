package service.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.notification.dto.event.VisitaEvent;
import service.notification.entity.VisitaRecordatorio;
import service.notification.enums.EstadoVisitaRecordatorio;
import service.notification.enums.TipoNotificacion;
import service.notification.repository.VisitaRecordatorioRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecordatorioVisitaService {

    private static final int HORAS_RECORDATORIO_LARGO = 24;
    private static final int HORAS_RECORDATORIO_CORTO = 1;

    private final VisitaRecordatorioRepository repository;
    private final NotificacionService notificacionService;

    @Transactional
    public void registrar(VisitaEvent evento) {
        if (repository.existsById(evento.visitaId()) || evento.fechaHora() == null) {
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();

        VisitaRecordatorio visita = VisitaRecordatorio.builder()
                .visitaId(evento.visitaId())
                .leadId(evento.leadId())
                .propiedadId(evento.propiedadId())
                .clienteId(evento.clienteId())
                .agenteId(evento.agenteId())
                .fechaHora(evento.fechaHora())
                .estado(EstadoVisitaRecordatorio.PROGRAMADA)
                .recordatorio24hEn(evento.fechaHora().isAfter(ahora.plusHours(HORAS_RECORDATORIO_LARGO)) ? null : ahora)
                .recordatorio1hEn(evento.fechaHora().isAfter(ahora.plusHours(HORAS_RECORDATORIO_CORTO)) ? null : ahora)
                .build();

        repository.save(visita);
    }

    @Transactional
    public void marcarCancelada(UUID visitaId) {
        repository.findById(visitaId).ifPresent(v -> v.setEstado(EstadoVisitaRecordatorio.CANCELADA));
    }

    @Transactional
    public void marcarRealizada(UUID visitaId) {
        repository.findById(visitaId).ifPresent(v -> v.setEstado(EstadoVisitaRecordatorio.REALIZADA));
    }

    @Transactional
    public void enviarRecordatoriosPendientes() {
        LocalDateTime ahora = LocalDateTime.now();

        for (VisitaRecordatorio visita : repository.pendientesDe24Horas(ahora, ahora.plusHours(HORAS_RECORDATORIO_LARGO))) {
            String mensaje = "Tienes una visita programada para el " + Formatos.fecha(visita.getFechaHora())
                    + ". Faltan menos de 24 horas.";
            avisar(visita, TipoNotificacion.RECORDATORIO_VISITA_24H, "RECORDATORIO_24H:", mensaje, true);
            visita.setRecordatorio24hEn(ahora);
        }

        for (VisitaRecordatorio visita : repository.pendientesDeUnaHora(ahora, ahora.plusHours(HORAS_RECORDATORIO_CORTO))) {
            String mensaje = "Tu visita programada para el " + Formatos.fecha(visita.getFechaHora())
                    + " comienza en aproximadamente 1 hora.";
            avisar(visita, TipoNotificacion.RECORDATORIO_VISITA_1H, "RECORDATORIO_1H:", mensaje, false);
            visita.setRecordatorio1hEn(ahora);
        }
    }

    private void avisar(VisitaRecordatorio visita, TipoNotificacion tipo, String prefijoClave,
                        String mensaje, boolean correo) {
        String clave = prefijoClave + visita.getVisitaId();
        notificacionService.notificar(visita.getClienteId(), tipo, "Recordatorio de visita", mensaje,
                visita.getVisitaId(), clave, correo);
        notificacionService.notificar(visita.getAgenteId(), tipo, "Recordatorio de visita", mensaje,
                visita.getVisitaId(), clave, correo);
    }
}
