package service.notification.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Formatos {

    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");
    private static final Locale COLOMBIA = Locale.of("es", "CO");

    private Formatos() {
    }

    public static String fecha(LocalDateTime fecha) {
        return fecha == null ? "una fecha por definir" : FECHA_HORA.format(fecha);
    }

    public static String monto(BigDecimal monto) {
        return monto == null ? "$0" : String.format(COLOMBIA, "$%,.2f", monto);
    }
}
