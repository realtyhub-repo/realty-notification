package service.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "realtyhub.events";
    public static final String DEAD_LETTER_EXCHANGE = "realtyhub.events.dlx";

    public static final String RK_LEAD_CREADO = "lead.creado";
    public static final String RK_LEAD_OFERTA_ACEPTADA = "lead.oferta_aceptada";
    public static final String RK_VISITA_AGENDADA = "visita.agendada";
    public static final String RK_VISITA_CANCELADA = "visita.cancelada";
    public static final String RK_VISITA_REALIZADA = "visita.realizada";
    public static final String RK_CONTRACT_SIGNED = "contract.signed";
    public static final String RK_CONTRACT_CANCELLED = "contract.cancelled";
    public static final String RK_COMMISSION_CREATED = "commission.created";
    public static final String RK_COMMISSION_APPROVED = "commission.approved";
    public static final String RK_COMMISSION_PAID = "commission.paid";
    public static final String RK_COMMISSION_ANNULLED = "commission.annulled";
    public static final String RK_COMMISSION_REVERTED = "commission.reverted";

    public static final String QUEUE_LEAD_CREADO = "notificaciones.lead-creado";
    public static final String QUEUE_LEAD_OFERTA_ACEPTADA = "notificaciones.lead-oferta-aceptada";
    public static final String QUEUE_VISITA_AGENDADA = "notificaciones.visita-agendada";
    public static final String QUEUE_VISITA_CANCELADA = "notificaciones.visita-cancelada";
    public static final String QUEUE_VISITA_REALIZADA = "notificaciones.visita-realizada";
    public static final String QUEUE_CONTRACT_SIGNED = "notificaciones.contract-signed";
    public static final String QUEUE_CONTRACT_CANCELLED = "notificaciones.contract-cancelled";
    public static final String QUEUE_COMMISSION_CREATED = "notificaciones.commission-created";
    public static final String QUEUE_COMMISSION_APPROVED = "notificaciones.commission-approved";
    public static final String QUEUE_COMMISSION_PAID = "notificaciones.commission-paid";
    public static final String QUEUE_COMMISSION_ANNULLED = "notificaciones.commission-annulled";
    public static final String QUEUE_COMMISSION_REVERTED = "notificaciones.commission-reverted";

    private static final String[][] ENLACES = {
            {QUEUE_LEAD_CREADO, RK_LEAD_CREADO},
            {QUEUE_LEAD_OFERTA_ACEPTADA, RK_LEAD_OFERTA_ACEPTADA},
            {QUEUE_VISITA_AGENDADA, RK_VISITA_AGENDADA},
            {QUEUE_VISITA_CANCELADA, RK_VISITA_CANCELADA},
            {QUEUE_VISITA_REALIZADA, RK_VISITA_REALIZADA},
            {QUEUE_CONTRACT_SIGNED, RK_CONTRACT_SIGNED},
            {QUEUE_CONTRACT_CANCELLED, RK_CONTRACT_CANCELLED},
            {QUEUE_COMMISSION_CREATED, RK_COMMISSION_CREATED},
            {QUEUE_COMMISSION_APPROVED, RK_COMMISSION_APPROVED},
            {QUEUE_COMMISSION_PAID, RK_COMMISSION_PAID},
            {QUEUE_COMMISSION_ANNULLED, RK_COMMISSION_ANNULLED},
            {QUEUE_COMMISSION_REVERTED, RK_COMMISSION_REVERTED}
    };

    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Declarables colasDeEventos(TopicExchange eventsExchange, DirectExchange deadLetterExchange) {
        List<Declarable> declarables = new ArrayList<>();
        for (String[] enlace : ENLACES) {
            String nombre = enlace[0];
            String routingKey = enlace[1];
            String nombreDlq = nombre + ".dlq";

            Queue cola = QueueBuilder.durable(nombre)
                    .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                    .deadLetterRoutingKey(nombreDlq)
                    .build();
            Queue colaMuerta = QueueBuilder.durable(nombreDlq).build();

            Binding enlaceCola = BindingBuilder.bind(cola).to(eventsExchange).with(routingKey);
            Binding enlaceMuerta = BindingBuilder.bind(colaMuerta).to(deadLetterExchange).with(nombreDlq);

            declarables.add(cola);
            declarables.add(colaMuerta);
            declarables.add(enlaceCola);
            declarables.add(enlaceMuerta);
        }
        return new Declarables(declarables);
    }
}
