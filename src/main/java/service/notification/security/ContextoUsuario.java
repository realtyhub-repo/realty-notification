package service.notification.security;

import service.notification.enums.RolUsuario;

import java.util.UUID;

public record ContextoUsuario(UUID userId, RolUsuario rol) {
}
