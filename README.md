# TODO

- Compilación nativa de imágenes Docker
    - compilar en nativo y construir las imagenes de todos los módulos
        - PENDING:
        - DONE:
            - scheduler
            - integration
            - statemachine
            - comunytek
            - catalog
            - telegramchatbot
            - telegramchatbotnotifier
            - web
            - core
            - commandprocessor
    - probar toda la aplicación con las imágenes nativas
- Prueba día completo
    - scheduler en ejecución
    - comunytek real
- Comprobación de estado
    - Verificar que funciona con varios usuarios
        - Después del cambio del ProduceMulti
    - Verificar que funciona cuando la notificacion es multiple
        - Registrar el mismo usuario en varios móviles
- Revisar Chatbot
    - Formato de mensajes
    - Contenido de mensajes
- Despliegue en producción
- Hacer repo privado
- *************** DEMO ***************

# Backlog

- Añadir métricas (https://quarkus.io/guides/microprofile-metrics)
- Monitorización de componentes con Graphana y Prometheus
- Crear un servicio con una cache para la recuperación de las preferencias de los worker (invalidar la cache al actualizar las preferencias)
- Permitir la actualización de las preferencias con un comando /update_settings
- Añadir la posibilidad de meter excepciones al horario habitual
    - cambiar widget
    - la excepcion tendrá un periodo de aplicación
    - Extraer las preferencias a una tabla de bdd separada (con fecha inicio y fecha fin) para poder tener periodos de tiempo custom.
    - Adaptar la recuperación de las preferencias actuales para tener en cuenta que puede haber periodos custom
- Crear pagina de administración (ej: reset de contraseñas de usuario, revisar mensajes basura)
    - Protegida con el perfil
    - Habrá comando /admin (no público) para acceder a la administración
    - El comando /admin genera un link incluyendo el registrationId
    - Al acceder al Admin con el registrationId se comprueba el perfil del usuario
    - Si el usuario no es admin -> UnauthorizedException
- Schedule
    - Añadir schedule para limpiar los trash_message
- Añadir servicio de estadísticas de llamadas a Comunytek (usar VertX)
- Revisar respuesta de stackoverflow
    - https://stackoverflow.com/questions/62483105/manage-acknowledge-with-mutiny-when-transforming-message-to-multimessage
    - ya no recuerdo donde se daba el caso
        - al notificar el estado? que entra un mensaje por usuario pero para cada usuario se va a notificar a todos sus chatIds
- Revisar el uso de .invoke() junto con .await().indefinitely()
- Revisar accesos a base de datos... cambiar DAO y usar los propios Bean para seleccionar (o hacerlo con transacciones para leer en una transacción)
- Web Registro
    - Mejorar la visualización de los errores: cuando afectan a varios campos, como por ejemplo, cuando el intervalo de trabajo no es correcto
- Web Demo
    - Al ejecutar a mano un batch, además de poner el spinner sobre el botón en cuestión, quitar el texto de la última ejecución
    - Quitar RxJs y dejar todo con JQuery (más fácil de mantener)
    - Crear componente JQuery para establecer la hora
- Meter test unitarios
    - Probar las validaciones con tests unitarios
- Icono TimeHammer
- Icono KronosTools
- Mejorar flujo de despliegue en producción
    - Estudiar uso de registry
        - Docker Hub?
        - Contenedor registry de docker en producción para publicar la imagen de timehammer?
    - Imagen nativa de timehammer
        - La compilación nativa no funciona con quarkus-camel-telegram
            - esperar a que haya versión final y volver a probar (en el iMac no tengo problemas para compilar)
            - cuando funciona luego no ejecuta en la imagen distroless del Dockerfile tiny
                - ya abrí un issue y me respondieron diciendo que se podía hacer
                - retomar el issue y pedir que lo añadan y lo publiquen
- Completar la página de FAQ
    - ¿Cómo de segura está mi contraseña?
- Despliegue en producción
    - Revisión de contenido de páginas
    - Política de privacidad y de cookies
- Endpoint para generar un token (a partir de un chatId)
- Proteger endpoint de reporte de ssid a través de un token
- Aplicación Android para hacer de forma periódica el envío del SSID?
    - La aplicación Android únicamente enviaría los eventos interesantes, de esta forma no se trackearía todos los SSID del usuario
    - Gestionaría los reenvíos en caso de no haber conexión
    - Gestionaría la obtención de token
- Añadir método para determinar la acción para un worker en base a sus SsidTrackingEvent (CONNECTED, DISCONNECTED, TICK)
- I18N para los HTML?
    - Las validaciones ya están en multiidoma (se podrían configurar distintos mensajes creando un fichero de properties para el idioma en cuestion)
---

# Referencias

## Hibernate - Relationships

https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/

## Hibernate - Enums

https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/

## Hibernate - HashCode and Equals

https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/
https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
https://vladmihalcea.com/the-best-way-to-map-a-naturalid-business-key-with-jpa-and-hibernate/
https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/

## Hibernate - Date

https://vladmihalcea.com/how-to-store-date-time-and-timestamps-in-utc-time-zone-with-jdbc-and-hibernate/

## Hibernate - Persist collections

https://vladmihalcea.com/merge-entity-collections-jpa-hibernate/

## Hibernate - Validations

https://dwuysan.wordpress.com/2012/03/20/cross-field-validation-using-bean-validation-jsr-303/amp/

## DTO Mappings

https://mapstruct.org/news/2019-12-06-mapstruct-and-quarkus/

## Front - Componente RangeSlider

http://ionden.com/a/plugins/ion.rangeSlider/index.html

## Quarkus - Cheatsheet

https://lordofthejars.github.io/quarkus-cheat-sheet

## Quarkus - Camel

https://camel.apache.org/camel-quarkus/latest/first-steps.html
https://camel.apache.org/camel-quarkus/latest/list-of-camel-quarkus-extensions.html
https://camel.apache.org/components/latest/telegram-component.html

## Telegram - API

https://core.telegram.org/bots/api

## NGINX + Let's encrypt

https://medium.com/@pentacent/nginx-and-lets-encrypt-with-docker-in-less-than-5-minutes-b4b8a60d3a71
https://thepolyglotdeveloper.com/2017/03/nginx-reverse-proxy-containerized-docker-applications

## Docker Compose - extending services

https://docs.docker.com/compose/extends

## Reactive

https://quarkus.io/guides/getting-started-reactive
https://quarkus.io/guides/reactive-routes
https://vertx.io/docs/vertx-web/java
https://quarkus.io/guides/kafka
https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/2/index.html
https://smallrye.io/smallrye-mutiny
https://vertx.io/docs/vertx-pg-client/java

---

# Creación de un proyecto quarkus.io

```
$ docker run -it --rm -v C:\work\repos\kronostools\timehammer\wksp\timehammer:/root/wksp/timehammer -v %USERPROFILE%\.m2\repository:/root/.m2/repository maven:3.6.3-jdk-11-slim bash
$ mvn io.quarkus:quarkus-maven-plugin:1.6.0.Final:create \
    -DprojectGroupId=com.kronostools.timehammer \
    -DprojectArtifactId=ssidtracking \
    -DclassName="com.diegocastroviadero.timehammer.ssidtracking.SSIDTrackingResource" \
    -Dpath="/trackSSID"
```

Para arrancar el proyecto quarkus.io:

```
$ mvn quarkus:dev
```

# Arranque

```
# run web service
$ docker-compose up web

# run db service
$ docker-compose up -d db
```

# PSQL commands

```
# listar bases de datos
$ \l

# cambiar de base de datos
$ \c <database>

# listar tablas
$ \dt

# listar secuencias
$ \ds

# describir tabla
$ \d <table>
```

# Build Native

Para compilar la imagen distroless que se usa para ejecutar las compilaciones nativas de quarkus

(https://github.com/GoogleContainerTools/distroless/tree/master/cc)

```
bazel-3.2.0 build --host_force_python=PY2 //package_manager:dpkg_parser.par
bazel-3.2.0 build --host_force_python=PY2 //cc:cc_debian10
bazel-3.2.0 run --host_force_python=PY2 //cc:cc_debian10
```

Retaggear la imagen distroless generada:

```
docker tag bazel/cc:cc_debian10 timehammer/base-debian10:1.0.0
```

Para compilar el código en nativo

```
$ docker-compose --file docker-compose.nativebuild.yml up catalog
```

Para construir imagen con el ejecutable nativo

```
> docker-compose --file docker-compose.native.yml build web
```

# Despliegue en producción

(a mejorar)

```
cd /home/timehammer/wksp/kronostools \
&& rm -Rf timehammer \
&& git clone https://github.com/kronostools/timehammer.git \
&& cd timehammer \
&& rm -f docker-compose.override.yml \
&& rm -Rf .git
```

Subir fichero `.env` (con filezilla)

docker-compose up -d db
docker-compose up -d timehammer
docker-compose up -d reverseproxy

(la idea sería que fuese necesario únicamente `docker-compose up -d`)