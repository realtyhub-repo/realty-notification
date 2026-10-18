package service.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.notification.dto.event.CommissionCreatedEvent;
import service.notification.dto.event.CommissionStatusEvent;
import service.notification.dto.event.ContractCancelledEvent;
import service.notification.dto.event.ContractSignedEvent;
import service.notification.dto.event.LeadCreadoEvent;
import service.notification.dto.event.LeadOfertaAceptadaEvent;
import service.notification.dto.event.VisitaCanceladaEvent;
import service.notification.dto.event.VisitaEvent;
import service.notification.enums.TipoNotificacion;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacionEventosService {

    private static final String MOTIVO_COMPENSACION = "COMPENSACION";
    private static final String MOTIVO_CLIENTE = "CLIENTE";
    private static final String MOTIVO_AGENTE = "AGENTE";
    private static final String MOTIVO_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String MOTIVO_LEAD_DESCARTADO = "LEAD_DESCARTADO";
    private static final String MOTIVO_PROPIEDAD_NO_DISPONIBLE = "PROPIEDAD_NO_DISPONIBLE";

    private final NotificacionService notificaciones;
    private final RecordatorioVisitaService recordatorios;

    @Transactional
    public void alCrearseLead(LeadCreadoEvent evento) {
        notificaciones.notificar(evento.agenteId(), TipoNotificacion.VISITA_AGENDADA,
                "Nuevo lead asignado",
                "Se te asignó un nuevo lead con una visita el " + Formatos.fecha(evento.fechaHoraVisita()) + ".",
                evento.visitaId(), "VISITA_AGENDADA:" + evento.visitaId(), true);
    }

    @Transactional
    public void alAceptarseOferta(LeadOfertaAceptadaEvent evento) {
        notificaciones.notificar(evento.clienteId(), TipoNotificacion.OFERTA_ACEPTADA,
                "Oferta aceptada",
                "Tu oferta fue aceptada. En breve se preparará el contrato.",
                evento.leadId(), "OFERTA_ACEPTADA:" + evento.leadId(), true);
    }

    @Transactional
    public void alAgendarseVisita(VisitaEvent evento) {
        String clave = "VISITA_AGENDADA:" + evento.visitaId();
        String fecha = Formatos.fecha(evento.fechaHora());

        notificaciones.notificar(evento.clienteId(), TipoNotificacion.VISITA_AGENDADA,
                "Visita agendada",
                "Tu visita quedó agendada para el " + fecha + ".",
                evento.visitaId(), clave, true);

        notificaciones.notificar(evento.agenteId(), TipoNotificacion.VISITA_AGENDADA,
                "Nueva visita asignada",
                "Tienes una visita agendada para el " + fecha + ".",
                evento.visitaId(), clave, true);

        recordatorios.registrar(evento);
    }

    @Transactional
    public void alCancelarseVisita(VisitaCanceladaEvent evento) {
        recordatorios.marcarCancelada(evento.visitaId());

        String motivo = evento.motivo() == null ? "" : evento.motivo();
        if (MOTIVO_COMPENSACION.equals(motivo)) {
            return;
        }

        String fecha = Formatos.fecha(evento.fechaHora());
        String clave = "VISITA_CANCELADA:" + evento.visitaId();

        String mensaje = switch (motivo) {
            case MOTIVO_CLIENTE -> "El cliente canceló la visita programada para el " + fecha + ".";
            case MOTIVO_AGENTE -> "El agente canceló la visita programada para el " + fecha + ".";
            case MOTIVO_ADMINISTRADOR -> "La visita programada para el " + fecha + " fue cancelada por la administración.";
            case MOTIVO_LEAD_DESCARTADO -> "La visita programada para el " + fecha + " fue cancelada porque el lead fue descartado.";
            case MOTIVO_PROPIEDAD_NO_DISPONIBLE -> "La visita programada para el " + fecha + " fue cancelada porque la propiedad ya no está disponible.";
            default -> "La visita programada para el " + fecha + " fue cancelada.";
        };

        if (!MOTIVO_CLIENTE.equals(motivo)) {
            notificaciones.notificar(evento.clienteId(), TipoNotificacion.VISITA_CANCELADA,
                    "Visita cancelada", mensaje, evento.visitaId(), clave, true);
        }
        if (!MOTIVO_AGENTE.equals(motivo)) {
            notificaciones.notificar(evento.agenteId(), TipoNotificacion.VISITA_CANCELADA,
                    "Visita cancelada", mensaje, evento.visitaId(), clave, true);
        }
    }

    @Transactional
    public void alRealizarseVisita(VisitaEvent evento) {
        recordatorios.marcarRealizada(evento.visitaId());
    }

    @Transactional
    public void alFirmarseContrato(ContractSignedEvent evento) {
        String clave = "CONTRATO_FIRMADO:" + evento.contratoId();
        String fecha = Formatos.fecha(evento.firmadoEn());

        notificaciones.notificar(evento.clienteId(), TipoNotificacion.CONTRATO_FIRMADO,
                "Contrato firmado",
                "Tu contrato quedó firmado el " + fecha + ".",
                evento.contratoId(), clave, true);

        notificaciones.notificar(evento.agenteId(), TipoNotificacion.CONTRATO_FIRMADO,
                "Contrato firmado",
                "El contrato de tu cliente quedó firmado el " + fecha + ".",
                evento.contratoId(), clave, true);
    }

    @Transactional
    public void alCancelarseContrato(ContractCancelledEvent evento) {
        String clave = "CONTRATO_CANCELADO:" + evento.contratoId();
        String mensaje = "El contrato asociado a tu negociación fue cancelado.";

        notificaciones.notificar(evento.clienteId(), TipoNotificacion.CONTRATO_CANCELADO,
                "Contrato cancelado", mensaje, evento.contratoId(), clave, true);
        notificaciones.notificar(evento.agenteId(), TipoNotificacion.CONTRATO_CANCELADO,
                "Contrato cancelado", mensaje, evento.contratoId(), clave, true);
    }

    @Transactional
    public void alCrearseComision(CommissionCreatedEvent evento) {
        notificaciones.notificar(evento.gerenteOficinaId(), TipoNotificacion.COMISION_CREADA,
                "Nueva comisión generada",
                "Se generó una comisión de " + Formatos.monto(evento.montoTotal()) + " en tu oficina.",
                evento.comisionId(), "COMISION_CREADA:" + evento.comisionId(), true);
    }

    @Transactional
    public void alAprobarseComision(CommissionStatusEvent evento) {
        notificaciones.notificar(evento.agenteId(), TipoNotificacion.COMISION_APROBADA,
                "Comisión aprobada",
                "Tu comisión de " + Formatos.monto(evento.montoTotal()) + " fue aprobada.",
                evento.comisionId(), "COMISION_APROBADA:" + evento.comisionId(), false);
    }

    @Transactional
    public void alPagarseComision(CommissionStatusEvent evento) {
        notificaciones.notificar(evento.agenteId(), TipoNotificacion.COMISION_PAGADA,
                "Comisión pagada",
                "Tu comisión de " + Formatos.monto(evento.montoTotal()) + " fue pagada.",
                evento.comisionId(), "COMISION_PAGADA:" + evento.comisionId(), true);
    }

    @Transactional
    public void alAnularseComision(CommissionStatusEvent evento) {
        avisarAgenteYGerente(evento, TipoNotificacion.COMISION_ANULADA, "Comisión anulada", "anulada",
                "COMISION_ANULADA:");
    }

    @Transactional
    public void alRevertirseComision(CommissionStatusEvent evento) {
        avisarAgenteYGerente(evento, TipoNotificacion.COMISION_REVERTIDA, "Comisión revertida", "revertida",
                "COMISION_REVERTIDA:");
    }

    private void avisarAgenteYGerente(CommissionStatusEvent evento, TipoNotificacion tipo, String titulo,
                                      String accion, String prefijoClave) {
        BigDecimal monto = evento.montoTotal();
        String mensaje = "La comisión de " + Formatos.monto(monto) + " fue " + accion + ".";
        String clave = prefijoClave + evento.comisionId();
        UUID referencia = evento.comisionId();

        notificaciones.notificar(evento.agenteId(), tipo, titulo, mensaje, referencia, clave, true);
        notificaciones.notificar(evento.gerenteOficinaId(), tipo, titulo, mensaje, referencia, clave, true);
    }
}
