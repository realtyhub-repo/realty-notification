package service.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.notification.dto.response.ContadorResponse;
import service.notification.dto.response.MarcadasResponse;
import service.notification.dto.response.NotificacionResponse;
import service.notification.dto.response.PaginaResponse;
import service.notification.security.ContextoUsuario;
import service.notification.security.UsuarioActual;
import service.notification.service.NotificacionService;

import java.util.UUID;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public PaginaResponse<NotificacionResponse> listar(
            @UsuarioActual ContextoUsuario contexto,
            @RequestParam(defaultValue = "false") boolean soloNoLeidas,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanio) {
        return notificacionService.listar(contexto.userId(), soloNoLeidas, pagina, tamanio);
    }

    @GetMapping("/no-leidas/contador")
    public ContadorResponse contarNoLeidas(@UsuarioActual ContextoUsuario contexto) {
        return notificacionService.contarNoLeidas(contexto.userId());
    }

    @PatchMapping("/{id}/leida")
    public NotificacionResponse marcarLeida(@UsuarioActual ContextoUsuario contexto, @PathVariable UUID id) {
        return notificacionService.marcarLeida(contexto.userId(), id);
    }

    @PatchMapping("/leer-todas")
    public MarcadasResponse marcarTodasLeidas(@UsuarioActual ContextoUsuario contexto) {
        return notificacionService.marcarTodasLeidas(contexto.userId());
    }
}
