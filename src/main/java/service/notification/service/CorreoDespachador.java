package service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.notification.config.NotificacionesProperties;
import service.notification.dto.response.UsuarioContactoInternalResponse;
import service.notification.entity.EnvioCorreo;
import service.notification.enums.EstadoEnvioCorreo;
import service.notification.exceptions.ServicioNoDisponibleException;
import service.notification.exceptions.UsuarioNoEncontradoException;
import service.notification.repository.EnvioCorreoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CorreoDespachador {

    private static final int LONGITUD_MAXIMA_ERROR = 500;
    private static final long ESPERA_MAXIMA_MINUTOS = 30;

    private final EnvioCorreoRepository repository;
    private final UsuarioClientService usuarioClient;
    private final JavaMailSender mailSender;
    private final NotificacionesProperties propiedades;

    @Scheduled(fixedDelayString = "${notificaciones.correo.intervalo-ms:15000}")
    public void despachar() {
        NotificacionesProperties.Correo config = propiedades.correo();
        if (!config.habilitado()) {
            return;
        }

        List<EnvioCorreo> pendientes = repository.buscarPendientes(
                LocalDateTime.now(), PageRequest.of(0, config.lote()));

        for (EnvioCorreo envio : pendientes) {
            procesar(envio, config);
        }
    }

    private void procesar(EnvioCorreo envio, NotificacionesProperties.Correo config) {
        try {
            UsuarioContactoInternalResponse contacto = usuarioClient.buscarContacto(envio.getUsuarioId());

            if (contacto.email() == null || contacto.email().isBlank()) {
                marcarFallido(envio, "El usuario no tiene correo registrado");
            } else {
                enviar(envio, contacto, config.remitente());
                envio.setIntentos(envio.getIntentos() + 1);
                envio.setEstado(EstadoEnvioCorreo.ENVIADO);
                envio.setEnviadoEn(LocalDateTime.now());
                envio.setUltimoError(null);
            }
        } catch (UsuarioNoEncontradoException e) {
            marcarFallido(envio, e.getMessage());
        } catch (ServicioNoDisponibleException | MailException e) {
            reintentarOFallar(envio, e.getMessage(), config.maxIntentos());
        }

        repository.save(envio);
    }

    private void enviar(EnvioCorreo envio, UsuarioContactoInternalResponse contacto, String remitente) {
        String nombre = contacto.nombre() == null || contacto.nombre().isBlank() ? "" : " " + contacto.nombre();

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(contacto.email());
        mensaje.setSubject(envio.getAsunto());
        mensaje.setText("Hola" + nombre + ",\n\n"
                + envio.getCuerpo()
                + "\n\nIngresa a RealtyHub para ver más detalles.\n\nEquipo RealtyHub");

        mailSender.send(mensaje);
    }

    private void marcarFallido(EnvioCorreo envio, String error) {
        envio.setIntentos(envio.getIntentos() + 1);
        envio.setEstado(EstadoEnvioCorreo.FALLIDO);
        envio.setUltimoError(recortar(error));
        log.warn("Correo {} descartado: {}", envio.getId(), error);
    }

    private void reintentarOFallar(EnvioCorreo envio, String error, int maxIntentos) {
        int intentos = envio.getIntentos() + 1;
        envio.setIntentos(intentos);
        envio.setUltimoError(recortar(error));

        if (intentos >= maxIntentos) {
            envio.setEstado(EstadoEnvioCorreo.FALLIDO);
            log.error("Correo {} falló tras {} intentos: {}", envio.getId(), intentos, error);
            return;
        }

        long espera = Math.min(ESPERA_MAXIMA_MINUTOS, (long) intentos * intentos);
        envio.setProximoIntentoEn(LocalDateTime.now().plusMinutes(espera));
        log.warn("Correo {} falló (intento {}), se reintentará en {} min: {}", envio.getId(), intentos, espera, error);
    }

    private String recortar(String texto) {
        if (texto == null) {
            return null;
        }
        return texto.length() <= LONGITUD_MAXIMA_ERROR ? texto : texto.substring(0, LONGITUD_MAXIMA_ERROR);
    }
}
