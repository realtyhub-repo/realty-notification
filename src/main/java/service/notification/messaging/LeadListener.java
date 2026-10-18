package service.notification.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import service.notification.config.RabbitConfig;
import service.notification.dto.event.LeadCreadoEvent;
import service.notification.dto.event.LeadOfertaAceptadaEvent;
import service.notification.service.NotificacionEventosService;

@Component
@RequiredArgsConstructor
public class LeadListener {

    private final NotificacionEventosService eventosService;

    @RabbitListener(queues = RabbitConfig.QUEUE_LEAD_CREADO)
    public void alCrearseLead(LeadCreadoEvent evento) {
        eventosService.alCrearseLead(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_LEAD_OFERTA_ACEPTADA)
    public void alAceptarseOferta(LeadOfertaAceptadaEvent evento) {
        eventosService.alAceptarseOferta(evento);
    }
}
