# Realty Notification

Realty Notification es el microservicio de RealtyHub encargado de gestionar el envío de notificaciones por correo electrónico y apoyar la comunicación entre los componentes de la plataforma.

## Funcionalidades

- Escucha los eventos de leads, visitas, contratos y comisiones para avisar a las partes involucradas.
- Bandeja de notificaciones dentro de la aplicación, con contador de no leídas.
- Envío de correos electrónicos con reintentos.
- Recordatorios automáticos de visitas (24 horas y 1 hora antes).

## Tecnologías

Java 21, Spring Boot, PostgreSQL, Flyway, RabbitMQ y Spring Mail.

## Puerto

El servicio se ejecuta en el puerto `8088`.
