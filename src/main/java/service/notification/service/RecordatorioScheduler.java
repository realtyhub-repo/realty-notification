package service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordatorioScheduler {

    private final RecordatorioVisitaService recordatorioService;

    @Scheduled(fixedDelayString = "${notificaciones.recordatorios.intervalo-ms:60000}")
    public void enviarRecordatorios() {
        try {
            recordatorioService.enviarRecordatoriosPendientes();
        } catch (RuntimeException e) {
            log.error("Fallo al procesar recordatorios de visitas: {}", e.getMessage());
        }
    }
}
