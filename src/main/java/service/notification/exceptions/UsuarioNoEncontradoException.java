package service.notification.exceptions;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
