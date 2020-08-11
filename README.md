# TODO

- Comprobación de estado
    - Verificar que funciona cuando la notificacion es multiple
        - Registrar el mismo usuario en varios móviles
- Preparar post en página personal
    - Crear un nuevo proyecto (del estilo al TFM)
    - Preparar slides en la página del proyecto (con el plugin JS que viene en la página personal)
- Preparar dashboard para control de módulos?? (serviría para presentación)
- Cerrar version 1.0.0-ALPHA
    - Renombrar versiones de pom de 1.0.0-SNAPSHOT a 1.0.0-ALPHA
    - Mergear rama develop -> master
    - Crear TAG 1.0.0-ALPHA en REPO
- *************** DEMO ***************

# Backlog

- Implementar la baja de un usuario (/unregister)
- Crear un servicio con una cache para la recuperación de las preferencias de los worker (invalidar la cache al actualizar las preferencias)
- Añadir métricas (https://quarkus.io/guides/microprofile-metrics)
- Monitorización de componentes
    - ¿Dashboard custom con D3.js?
    - ¿con Graphana y Prometheus?
- Web Registro
    - Mejorar la visualización de los errores: cuando afectan a varios campos, como por ejemplo, cuando el intervalo de trabajo no es correcto
- Permitir la actualización de las preferencias con un comando /update_settings
- Fix en la página de actualizar contraseña: a pantalla completa en pc se ve descentrado el formulario (está a la izquierda)
- Crear pagina de administración (ej: reset de contraseñas de usuario, revisar mensajes basura)
    - Protegida con el perfil
    - Habrá comando /admin (no público) para acceder a la administración
    - El comando /admin genera un link incluyendo el registrationId
    - Al acceder al Admin con el registrationId se comprueba el perfil del usuario
    - Si el usuario no es admin -> UnauthorizedException
- Añadir la posibilidad de meter excepciones al horario habitual
    - cambiar widget
    - la excepcion tendrá un periodo de aplicación
    - Extraer las preferencias a una tabla de bdd separada (con fecha inicio y fecha fin) para poder tener periodos de tiempo custom.
    - Adaptar la recuperación de las preferencias actuales para tener en cuenta que puede haber periodos custom
- Schedule
    - Añadir schedule para limpiar los trash_message
- Añadir servicio de estadísticas de llamadas a Comunytek
- Revisar respuesta de stackoverflow
    - https://stackoverflow.com/questions/62483105/manage-acknowledge-with-mutiny-when-transforming-message-to-multimessage
    - ya no recuerdo donde se daba el caso
        - al notificar el estado? que entra un mensaje por usuario pero para cada usuario se va a notificar a todos sus chatIds
- Revisar el uso de .invoke() junto con .await().indefinitely()
    - genera problemas cuando se mezcla código procedimental con código reactivo
- Revisar accesos a base de datos... cambiar DAO y usar los propios Bean para seleccionar (o hacerlo con transacciones para leer en una transacción)
- Web Demo
    - Al ejecutar a mano un batch, además de poner el spinner sobre el botón en cuestión, quitar el texto de la última ejecución
    - Quitar RxJs y dejar todo con JQuery (más fácil de mantener)
    - Crear componente JQuery para establecer la hora
    - Proteger el acceso (o llamadas al backend) en base al perfil
- Meter test unitarios
    - Probar las validaciones con tests unitarios
- Icono TimeHammer
- Icono KronosTools
- Revisar si ha salido la versión final de camel (actualmente 1.0.0-CR3)
- Mejorar flujo de despliegue en producción
    - Estudiar uso de registry
        - Docker Hub?
        - Contenedor registry de docker en producción para publicar la imagen de timehammer?
- Completar la página de FAQ
    - ¿Cómo de segura está mi contraseña?
    - ¿Por qué de vez en cuando tengo que volver a introducir mi contraseña?
- Despliegue en producción
    - Revisión de contenido de páginas
    - Política de privacidad y de cookies
    - Revisar Chatbot
        - Formato de mensajes
        - Contenido de mensajes
- Tras procesar una answer ¿haría falta llamar a `answerCallbackQuery`? ¿Al llamar a este método se borra el teclado? ¿Cómo se visualizan las alertas/notificaciones al llamar a este método?
    - Actualmente se está modificando el mensaje para quitarle el teclado, lo cual implica una gestión extra, que consiste en guardar en una cache los identificadores de los mensajes enviados con teclado para luego poder modificarlos
    - ¿Se podría simplificar este comportamiento usando `answerCallbackQuery`?
    - Igual se puede probar con el postman
- Investigar por qué cuando está sin usar un rato el chatbot, la primera vez tarda en responder bastante
    - Creo que también pasa cuando lo arranco en local, podría ser por el long polling o por cómo camel recupera los mensajes
        - La configuración del componente está disponible [aqui](https://camel.apache.org/components/latest/telegram-component.html#_query_parameters_32_parameters)
    - Es raro, porque si se envían mensajes seguidos, las respuestas son casi instantáneas
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

exportar las imágenes

```
docker image save --output wksp\images\telegramchatbotnotifier-1.0.0.tar timehammer/telegramchatbotnotifier:1.0.0
docker image save --output wksp\images\telegramchatbot-1.0.0.tar timehammer/telegramchatbot:1.0.0
docker image save --output wksp\images\statemachine-1.0.0.tar timehammer/statemachine:1.0.0
docker image save --output wksp\images\commandprocessor-1.0.0.tar timehammer/commandprocessor:1.0.0
docker image save --output wksp\images\comunytek-1.0.0.tar timehammer/comunytek:1.0.0
docker image save --output wksp\images\integration-1.0.0.tar timehammer/integration:1.0.0
docker image save --output wksp\images\catalog-1.0.0.tar timehammer/catalog:1.0.0
docker image save --output wksp\images\core-1.0.0.tar timehammer/core:1.0.0
docker image save --output wksp\images\web-1.0.0.tar timehammer/web:1.0.0
docker image save --output wksp\images\scheduler-1.0.0.tar timehammer/scheduler:1.0.0
```

subir imágenes al servidor

```
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\telegramchatbotnotifier-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\telegramchatbot-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\statemachine-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\commandprocessor-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\comunytek-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\integration-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\catalog-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\core-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\web-1.0.0.tar timehammer@54.37.152.149:/tmp
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\images\scheduler-1.0.0.tar timehammer@54.37.152.149:/tmp
```

cargar las imágenes el el registry local

```
docker load --input /tmp/telegramchatbotnotifier-1.0.0.tar
docker load --input /tmp/telegramchatbot-1.0.0.tar
docker load --input /tmp/statemachine-1.0.0.tar
docker load --input /tmp/commandprocessor-1.0.0.tar
docker load --input /tmp/comunytek-1.0.0.tar
docker load --input /tmp/integration-1.0.0.tar
docker load --input /tmp/catalog-1.0.0.tar
docker load --input /tmp/core-1.0.0.tar
docker load --input /tmp/web-1.0.0.tar
docker load --input /tmp/scheduler-1.0.0.tar
```

Preparar estructura de contenido:

```
mkdir -p /home/timehammer/wksp/kronostools/timehammer/wksp/reverseproxy/config/nginx/proxy-confs
```

Subir configuración `reverseproxy`:

```
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\reverseproxy\config\nginx\proxy-confs\timehammer.subdomain.conf timehammer@54.37.152.149:/home/timehammer/wksp/kronostools/timehammer/wksp/reverseproxy/config/nginx/proxy-confs
```

Subir `docker-compose`:

```
scp -i %USERPROFILE%/.ssh/timehammer.ovh docker-compose.nativeremote.yml timehammer@54.37.152.149:/home/timehammer/wksp/kronostools/timehammer/docker-compose.yml
```

Subir configuración `docker-compose`:

```
scp -i %USERPROFILE%/.ssh/timehammer.ovh .env timehammer@54.37.152.149:/home/timehammer/wksp/kronostools/timehammer
```

**Verificar el perfil configurado en el fichero de configuracion `.env`**

Posicionarse en la siguiente ruta:

```
cd /home/timehammer/wksp/kronostools/timehammer
```

Arrancar servicios:

```
docker-compose up -d db kafka
docker-compose up -d scheduler web core catalog integration comunytek telegramchatbot commandprocessor telegramchatbotnotifier statemachine
docker-compose up -d reverseproxy
```

Parar servicios:

```
docker-compose stop scheduler web core catalog integration comunytek telegramchatbot commandprocessor telegramchatbotnotifier statemachine
docker-compose rm -f scheduler web core catalog integration comunytek telegramchatbot commandprocessor telegramchatbotnotifier statemachine

docker-compose stop db kafka zookeeper
docker-compose rm -f db kafka zookeeper
```

## FAQ

### Para recargar la configuración de nginx (reverseproxy)

Desde dentro del contenedor, ejecutar:

```
s6-svc -h /var/run/s6/services/nginx
```

### Obtener consumo de CPU y memoria de los contenedores

[https://docs.docker.com/engine/reference/commandline/stats]

```
docker stats --format "table {{.ID}}\t{{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

### Mostrar el contenido de un volumen

```
docker run --rm -it -v timehammer_comunytekdata:/vol alpine:latest ls -al /vol
```