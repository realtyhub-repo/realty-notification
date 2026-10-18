CREATE TABLE notificacion (
    id            UUID         PRIMARY KEY,
    usuario_id    UUID         NOT NULL,
    tipo          VARCHAR(40)  NOT NULL,
    titulo        VARCHAR(150) NOT NULL,
    mensaje       VARCHAR(500) NOT NULL,
    referencia_id UUID,
    clave_dedupe  VARCHAR(120) NOT NULL,
    leida         BOOLEAN      NOT NULL DEFAULT FALSE,
    leida_en      TIMESTAMP,
    creada_en     TIMESTAMP    NOT NULL,
    CONSTRAINT uk_notificacion_usuario_clave UNIQUE (usuario_id, clave_dedupe)
);

CREATE INDEX idx_notificacion_usuario_fecha ON notificacion (usuario_id, creada_en DESC);
CREATE INDEX idx_notificacion_usuario_no_leidas ON notificacion (usuario_id) WHERE leida = FALSE;

CREATE TABLE envio_correo (
    id                  UUID         PRIMARY KEY,
    notificacion_id     UUID         NOT NULL REFERENCES notificacion (id),
    usuario_id          UUID         NOT NULL,
    asunto              VARCHAR(200) NOT NULL,
    cuerpo              VARCHAR(500) NOT NULL,
    estado              VARCHAR(20)  NOT NULL,
    intentos            INTEGER      NOT NULL DEFAULT 0,
    ultimo_error        VARCHAR(500),
    proximo_intento_en  TIMESTAMP    NOT NULL,
    enviado_en          TIMESTAMP,
    creado_en           TIMESTAMP    NOT NULL,
    CONSTRAINT uk_envio_correo_notificacion UNIQUE (notificacion_id)
);

CREATE INDEX idx_envio_correo_pendientes ON envio_correo (proximo_intento_en) WHERE estado = 'PENDIENTE';

CREATE TABLE visita_recordatorio (
    visita_id           UUID        PRIMARY KEY,
    lead_id             UUID        NOT NULL,
    propiedad_id        UUID        NOT NULL,
    cliente_id          UUID        NOT NULL,
    agente_id           UUID        NOT NULL,
    fecha_hora          TIMESTAMP   NOT NULL,
    estado              VARCHAR(20) NOT NULL,
    recordatorio_24h_en TIMESTAMP,
    recordatorio_1h_en  TIMESTAMP,
    creado_en           TIMESTAMP   NOT NULL,
    actualizado_en      TIMESTAMP   NOT NULL
);

CREATE INDEX idx_visita_recordatorio_programadas ON visita_recordatorio (fecha_hora) WHERE estado = 'PROGRAMADA';
