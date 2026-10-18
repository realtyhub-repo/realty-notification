package service.notification.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import service.notification.config.RabbitConfig;
import service.notification.dto.event.VisitaCanceladaEvent;
import service.notification.dto.event.VisitaEvent;
import service.notification.service.NotificacionEventosService;

@Component
@RequiredArgsConstructor
public class VisitaListener {

    private final NotificacionEventosService eventosService;

    @RabbitListener(queues = RabbitConfig.QUEUE_VISITA_AGENDADA)
    public void alAgendarseVisita(VisitaEvent evento) {
        eventosService.alAgendarseVisita(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_VISITA_CANCELADA)
    public void alCancelarseVisita(VisitaCanceladaEvent evento) {
        eventosService.alCancelarseVisita(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_VISITA_REALIZADA)
    public void alRealizarseVisita(VisitaEvent evento) {
        eventosService.alRealizarseVisita(evento);
    }
}
