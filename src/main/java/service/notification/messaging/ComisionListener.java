package service.notification.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import service.notification.config.RabbitConfig;
import service.notification.dto.event.CommissionCreatedEvent;
import service.notification.dto.event.CommissionStatusEvent;
import service.notification.service.NotificacionEventosService;

@Component
@RequiredArgsConstructor
public class ComisionListener {

    private final NotificacionEventosService eventosService;

    @RabbitListener(queues = RabbitConfig.QUEUE_COMMISSION_CREATED)
    public void alCrearseComision(CommissionCreatedEvent evento) {
        eventosService.alCrearseComision(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_COMMISSION_APPROVED)
    public void alAprobarseComision(CommissionStatusEvent evento) {
        eventosService.alAprobarseComision(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_COMMISSION_PAID)
    public void alPagarseComision(CommissionStatusEvent evento) {
        eventosService.alPagarseComision(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_COMMISSION_ANNULLED)
    public void alAnularseComision(CommissionStatusEvent evento) {
        eventosService.alAnularseComision(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_COMMISSION_REVERTED)
    public void alRevertirseComision(CommissionStatusEvent evento) {
        eventosService.alRevertirseComision(evento);
    }
}
