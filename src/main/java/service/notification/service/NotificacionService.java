package service.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.notification.dto.response.ContadorResponse;
import service.notification.dto.response.MarcadasResponse;
import service.notification.dto.response.NotificacionResponse;
import service.notification.dto.response.PaginaResponse;
import service.notification.entity.EnvioCorreo;
import service.notification.entity.Notificacion;
import service.notification.enums.EstadoEnvioCorreo;
import service.notification.enums.TipoNotificacion;
import service.notification.exceptions.NotificacionNoEncontradaException;
import service.notification.repository.EnvioCorreoRepository;
import service.notification.repository.NotificacionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private static final int TAMANIO_MAXIMO = 100;

    private final NotificacionRepository notificacionRepository;
    private final EnvioCorreoRepository envioCorreoRepository;

    @Transactional
    public void notificar(UUID usuarioId, TipoNotificacion tipo, String titulo, String mensaje,
                          UUID referenciaId, String claveDedupe, boolean enviarCorreo) {

        if (usuarioId == null) {
            return;
        }

        UUID id = UUID.randomUUID();
        LocalDateTime ahora = LocalDateTime.now();

        int insertadas = notificacionRepository.insertarSiNoExiste(
                id, usuarioId, tipo.name(), titulo, mensaje, referenciaId, claveDedupe, ahora);

        if (insertadas == 0 || !enviarCorreo) {
            return;
        }

        envioCorreoRepository.save(EnvioCorreo.builder()
                .notificacionId(id)
                .usuarioId(usuarioId)
                .asunto("[RealtyHub] " + titulo)
                .cuerpo(mensaje)
                .estado(EstadoEnvioCorreo.PENDIENTE)
                .intentos(0)
                .proximoIntentoEn(ahora)
                .creadoEn(ahora)
                .build());
    }

    @Transactional(readOnly = true)
    public PaginaResponse<NotificacionResponse> listar(UUID usuarioId, boolean soloNoLeidas, int pagina, int tamanio) {
        PageRequest pageable = PageRequest.of(Math.max(0, pagina), Math.min(TAMANIO_MAXIMO, Math.max(1, tamanio)));

        Page<Notificacion> resultado = soloNoLeidas
                ? notificacionRepository.findByUsuarioIdAndLeidaFalseOrderByCreadaEnDesc(usuarioId, pageable)
                : notificacionRepository.findByUsuarioIdOrderByCreadaEnDesc(usuarioId, pageable);

        return PaginaResponse.de(resultado.map(NotificacionResponse::from));
    }

    @Transactional(readOnly = true)
    public ContadorResponse contarNoLeidas(UUID usuarioId) {
        return new ContadorResponse(notificacionRepository.countByUsuarioIdAndLeidaFalse(usuarioId));
    }

    @Transactional
    public NotificacionResponse marcarLeida(UUID usuarioId, UUID notificacionId) {
        Notificacion notificacion = notificacionRepository.findByIdAndUsuarioId(notificacionId, usuarioId)
                .orElseThrow(() -> new NotificacionNoEncontradaException(
                        "No existe la notificación " + notificacionId));

        if (!notificacion.isLeida()) {
            notificacion.setLeida(true);
            notificacion.setLeidaEn(LocalDateTime.now());
        }
        return NotificacionResponse.from(notificacion);
    }

    @Transactional
    public MarcadasResponse marcarTodasLeidas(UUID usuarioId) {
        return new MarcadasResponse(notificacionRepository.marcarTodasLeidas(usuarioId, LocalDateTime.now()));
    }
}
