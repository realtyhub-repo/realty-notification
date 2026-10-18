package service.notification.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import service.notification.dto.internal.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ContextoUsuarioInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleContextoInvalido(ContextoUsuarioInvalidoException ex) {
        return construir(HttpStatus.UNAUTHORIZED, "CONTEXTO_INVALIDO", ex.getMessage());
    }

    @ExceptionHandler(NotificacionNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleNoEncontrada(NotificacionNoEncontradaException ex) {
        return construir(HttpStatus.NOT_FOUND, "NOTIFICACION_NO_ENCONTRADA", ex.getMessage());
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorResponse> handleSolicitudMalFormada(Exception ex) {
        return construir(HttpStatus.BAD_REQUEST, "SOLICITUD_INVALIDA",
                "La solicitud tiene parámetros faltantes o con formato inválido");
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String codigo, String mensaje) {
        ErrorResponse error = new ErrorResponse(codigo, mensaje, status.value(), LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
