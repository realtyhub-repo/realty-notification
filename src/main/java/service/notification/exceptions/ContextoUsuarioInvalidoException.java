package service.notification.exceptions;

public class ContextoUsuarioInvalidoException extends RuntimeException {

    public ContextoUsuarioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
