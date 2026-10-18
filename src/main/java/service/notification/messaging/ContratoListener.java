package service.notification.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import service.notification.config.RabbitConfig;
import service.notification.dto.event.ContractCancelledEvent;
import service.notification.dto.event.ContractSignedEvent;
import service.notification.service.NotificacionEventosService;

@Component
@RequiredArgsConstructor
public class ContratoListener {

    private final NotificacionEventosService eventosService;

    @RabbitListener(queues = RabbitConfig.QUEUE_CONTRACT_SIGNED)
    public void alFirmarseContrato(ContractSignedEvent evento) {
        eventosService.alFirmarseContrato(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_CONTRACT_CANCELLED)
    public void alCancelarseContrato(ContractCancelledEvent evento) {
        eventosService.alCancelarseContrato(evento);
    }
}
