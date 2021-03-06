# TODO

*************** DEMO ***************

- Cerrar version 1.0.0-ALPHA
    - Renombrar versiones de pom de 1.0.0-SNAPSHOT a 1.0.0-ALPHA
    - Crear TAG 1.0.0-ALPHA en REPO
    - Generar las imágenes de docker con versión 1.0.0-ALPHA

# Backlog

- Reducción de llamadas a Comunytek
    - Crear tabla con columnas (worker_internal_id, status, status_last_check, pending_action, pending_action_last_update)
      | worker_internal_id | status       | status_last_check | pending_action | pending_action_last_update |
      |--------------------|--------------|-------------------|----------------|----------------------------|
      | 1                  | initial      | 1-1-1900          | work_start     | 9-9-2020  8:00             |
      | 1                  | started      | 9-9-2020 8:02     | work_start     | 9-9-2020  8:00             |
      | 1                  | started      | 9-9-2020 8:02     | lunch_start    | 9-9-2020 13:00             |
      | 1                  | paused_lunch | 9-9-2020 13:02    | lunch_start    | 9-9-2020 12:50             |
- Implementar la baja de un usuario (/unregister)
- Privacidad de código?
    - Repo Git privado? Migrar a BitBucket?
    - Repo Docker Hub privado?
- Preparar diagrama con arquitectura de colas y módulos
- Migrar a Kubernetes (más complicado que Swarm, pero más flexible y es hacia donde va el mercado, más ejemplos, soporte, comunidad)
    - Crear los manifests de los dicerentes Deployments, Services, etc.
    - Hacer uso de los Secrets (y ConfigMaps)
    - Probar a instalar el Dashboard de Kubernetes
        - Probar también Portainer para kubernetes?
    - Base de datos
        - O se externaliza (y en kubernetes se podría crear un Service de tipo ExternalName)
        - O se despliega en kubernetes
            - Se puede utilizar un persistent volume local (jugando con los label y los taints para "reservar" un nodo para la base de datos)
            - Se puede tratar de crear un persistent volume distribuido de tal forma que no importe que se mueva de nodo (mirar también el controller StatefulSet o alternativa)
                - https://github.com/longhorn/longhorn
    - Kafka
        - Revisar la persistencia, si hace falta (que supongo que debería, para poder llevar el control de los mensajes procesados y pendientes), estamos ante la misma situación que la base de datos, aunque peor, porque externalizarlo no es tan fácil
        - Modo cluster, debería haber 3 instancias
    - Zookeeper
        - Cambiarlo a modo cluster, debería haber 3 instancias (2GB de RAM cada una? me parece un poco excesivo para el uso que se le va a dar)
            - Podría haber 3 Instancias de 2GB tanto para los zookeeper como para los kafka
    - Para el enrutado utilizar un Controller Ingress
        - Service Mesh?
        - Istio?
        - Istio y Kafka qué tal se llevan?
        - Se puede prescindir del NGINX?
        - Cómo se gestionan los certificados de Letsencrypt?
        - Istio aplica? No son microservicios HTTP
- Migrar a cloud
    - Clouds candidatos:
        - RPi 4
            - hardware
                - [Pagina interesante con serie en youtube](https://www.jeffgeerling.com/blog/2020/raspberry-pi-cluster-episode-6-turing-pi-review)
                - [switch netgear 8 poe 120W](https://www.amazon.es/gp/product/B076BV421P/ref=ox_sc_act_title_1?smid=A1AT7YVPFBWXBL&psc=1)
                - [rpi poe hat](https://www.tiendatec.es/raspberry-pi/hats/757-raspberry-pi-hat-poe-r20-0652508442105.html?src=raspberrypi)
                - [rpi 4 model b 8GB RAM](https://www.tiendatec.es/raspberry-pi/placas-base/1231-raspberry-pi-4-modelo-b-8gb-765756931199.html?src=raspberrypi)
            - un único gasto
            - más control
            - Construir la imagen strimzi/kafka para arm64
            - referencias
                - [resolver problemas de calor con rpi4](https://raspberryparatorpes.net/raspbian-2/nuevo-firmware-para-raspberry-pi-4/)
                - https://opensource.com/article/20/6/kubernetes-raspberry-pi
                - https://opensource.com/article/20/5/create-simple-cloud-init-service-your-homelab
                - https://opensource.com/article/20/5/nfs-raspberry-pi
                - https://opensource.com/article/20/6/kubernetes-nfs-client-provisioning
                - https://opensource.com/article/19/3/how-run-postgresql-kubernetes
                    - https://crunchydata.github.io/postgres-operator/stable/#documentation
                - https://opensource.com/article/20/8/ingress-controllers-kubernetes
                - https://opensource.com/article/20/7/homelab-metallb
                - https://opensource.com/article/20/6/kubernetes-lens
                - https://opensource.com/article/20/3/kubernetes-traefik
                - https://opensource.com/article/19/3/getting-started-jaeger
                - https://opensource.com/article/19/2/scaling-postgresql-kubernetes-operators
                - más:
                    https://opensource.com/tags/kubernetes
        - DigitalOcean
            - parece más pro que OVH
            - tengo este descuento (https://www.digitalocean.com/?refcode=ee97875d52fa&utm_campaign=Referral_Invite&utm_medium=Referral_Program&utm_source=CopyPaste)
        - AWS
            - más complicado que OVH
            - diría que más caro que OVH
    - Hace falta disponer de un LoadBalancer en el Cloud para poder acceder desde fuera
    - Para poder utilizar kubernetes en el cloud hay que publicar las imágenes en un Registry
        - Se utiliza Docker Hub
            - Para publicar imágenes de forma pública no hay problema
            - Se puede crear un repo privado de forma gratuita
            - Si se necesitan más repos privados son 5$/mo
        - Se despliega un registry privado
            - Más complejidad
            - Tendría el coste de una máquina (que va a ser muy cercano al coste de Docker Hub de pago)
            - En este caso se utilizaría ([Docker Distribution](https://github.com/docker/distribution) + [Portus](http://port.us.org))
- Crear un servicio con una cache para la recuperación de las preferencias de los worker (invalidar la cache al actualizar las preferencias)
- Añadir métricas (https://quarkus.io/guides/microprofile-metrics)
- Monitorización de componentes
    - ¿Dashboard custom con D3.js?
    - ¿con Graphana y Prometheus? 
- Gestión de componentes
    - [Gestión de Docker/Swarm/Kubernetes](https://www.portainer.io/installation/)
- Web Registro
    - Mejorar la visualización de los errores: cuando afectan a varios campos, como por ejemplo, cuando el intervalo de trabajo no es correcto
- Permitir la actualización de las preferencias con un comando /update_settings
- Configurar el User-Agent en las llamadas http
    - Deshabilitar el por defecto
    - Tener una lista de headers y en cada llamada coger uno random
- Externalizar la página de demo a un módulo independiente
- Comprobación de estado
    - Verificar que funciona cuando la notificacion es multiple
        - Registrar el mismo usuario en varios móviles
- Crear página de administración (ej: reset de contraseñas de usuario, revisar mensajes basura)
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
- Actualizar a la versión final de camel (actualmente 1.0.1)
- Completar la página de FAQ
    - ¿Cómo de segura está mi contraseña?
    - ¿Por qué de vez en cuando tengo que volver a introducir mi contraseña?
- Despliegue en producción
    - Revisión de contenido de páginas
    - Política de privacidad y de cookies
    - Revisar Chatbot
        - Formato de mensajes
        - Contenido de mensajes
- Actualizar Quarkus (a 14/08/2020 van por la 1.7.0)
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

# Compilación java

Compilar el código java

```
docker-compose -f docker-compose.compile.yml up core
docker-compose -f docker-compose.compile.yml up comunytek
docker-compose -f docker-compose.compile.yml up statemachine
docker-compose -f docker-compose.compile.yml up scheduler

docker-compose -f docker-compose.compile.yml up telegramchatbotnotifier
docker-compose -f docker-compose.compile.yml up telegramchatbot
docker-compose -f docker-compose.compile.yml up commandprocessor
docker-compose -f docker-compose.compile.yml up integration
docker-compose -f docker-compose.compile.yml up catalog
docker-compose -f docker-compose.compile.yml up web
```

Construir las imágenes

```
docker-compose -f docker-compose.imgbuild.yml build core
docker-compose -f docker-compose.imgbuild.yml build comunytek
docker-compose -f docker-compose.imgbuild.yml build statemachine
docker-compose -f docker-compose.imgbuild.yml build scheduler

docker-compose -f docker-compose.imgbuild.yml build telegramchatbotnotifier
docker-compose -f docker-compose.imgbuild.yml build telegramchatbot
docker-compose -f docker-compose.imgbuild.yml build commandprocessor
docker-compose -f docker-compose.imgbuild.yml build integration
docker-compose -f docker-compose.imgbuild.yml build catalog
docker-compose -f docker-compose.imgbuild.yml build web
```

Publicar imágenes en *Docker Hub*

```
docker push qopuir/timehammer-core:1.0.0-SNAPSHOT
docker push qopuir/timehammer-comunytek:1.0.0-SNAPSHOT
docker push qopuir/timehammer-statemachine:1.0.0-SNAPSHOT
docker push qopuir/timehammer-scheduler:1.0.0-SNAPSHOT

docker push qopuir/timehammer-telegramchatbotnotifier:1.0.0-SNAPSHOT
docker push qopuir/timehammer-telegramchatbot:1.0.0-SNAPSHOT
docker push qopuir/timehammer-commandprocessor:1.0.0-SNAPSHOT
docker push qopuir/timehammer-integration:1.0.0-SNAPSHOT
docker push qopuir/timehammer-catalog:1.0.0-SNAPSHOT
docker push qopuir/timehammer-web:1.0.0-SNAPSHOT
```

# Compilación en nativo

### Construir imagen base

Se utiliza una imagen distroless para la ejecución del código Quarkus compilado en nativo. La imagen que se utiliza es la de [Google](https://github.com/GoogleContainerTools/distroless/tree/master/cc) con alguna pequeña modificación sobre las librerías que lleva instaladas.

Para construir la imagen de google hay que utilizar `bazel` versión `3.2.0` (a día 01/08/2020). Los comandos a ejecutar son los siguientes:

```
bazel-3.2.0 build --host_force_python=PY2 //package_manager:dpkg_parser.par
bazel-3.2.0 build --host_force_python=PY2 //cc:cc_debian10
bazel-3.2.0 run --host_force_python=PY2 //cc:cc_debian10
```

Finalmente se retaggea la imagen distroless generada:

```
docker tag bazel/cc:cc_debian10 timehammer/base-debian10:1.0.0
```

### Compilar el código Quarkus en nativo

Para compilar el código en nativo

```
docker-compose -f docker-compose.compilenative.yml up telegramchatbotnotifier
docker-compose -f docker-compose.compilenative.yml up telegramchatbot
docker-compose -f docker-compose.compilenative.yml up statemachine
docker-compose -f docker-compose.compilenative.yml up commandprocessor
docker-compose -f docker-compose.compilenative.yml up comunytek
docker-compose -f docker-compose.compilenative.yml up integration
docker-compose -f docker-compose.compilenative.yml up catalog
docker-compose -f docker-compose.compilenative.yml up core
docker-compose -f docker-compose.compilenative.yml up web
docker-compose -f docker-compose.compilenative.yml up scheduler
```

Para construir imagen con el ejecutable nativo

```
docker-compose -f docker-compose.imgbuildnative.yml build telegramchatbotnotifier
docker-compose -f docker-compose.imgbuildnative.yml build telegramchatbot
docker-compose -f docker-compose.imgbuildnative.yml build statemachine
docker-compose -f docker-compose.imgbuildnative.yml build commandprocessor
docker-compose -f docker-compose.imgbuildnative.yml build comunytek
docker-compose -f docker-compose.imgbuildnative.yml build integration
docker-compose -f docker-compose.imgbuildnative.yml build catalog
docker-compose -f docker-compose.imgbuildnative.yml build core
docker-compose -f docker-compose.imgbuildnative.yml build web
docker-compose -f docker-compose.imgbuildnative.yml build scheduler
```

Publicar imágenes en *Docker Hub*

```
docker push qopuir/timehammer-telegramchatbotnotifier-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-telegramchatbot-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-statemachine-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-commandprocessor-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-comunytek-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-integration-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-catalog-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-core-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-web-native:1.0.0-SNAPSHOT
docker push qopuir/timehammer-scheduler-native:1.0.0-SNAPSHOT
```

# Despliegue en producción

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
# java
scp -i %USERPROFILE%/.ssh/timehammer.ovh docker-compose.runremote.yml timehammer@54.37.152.149:/home/timehammer/wksp/kronostools/timehammer/docker-compose.yml

# nativo
scp -i %USERPROFILE%/.ssh/timehammer.ovh docker-compose.runremotenative.yml timehammer@54.37.152.149:/home/timehammer/wksp/kronostools/timehammer/docker-compose.native.yml
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

# DEMO desde pc personal

Exponer puerto 8090 a través de `ngrok`:

```
ngrok http 8090
```

Copiar la url de forwarding a .env (ej: https://8ecf39152302.ngrok.io)

Arrancar kafka y db:

```
docker-compose -f docker-compose.runlocal.yml up -d db kafka
```

Limpiar la base de datos:

```
# docker-compose -f docker-compose.runlocal.yml exec db psql -U timehammer
delete from worker_holiday;
delete from worker_preferences;
delete from worker_chat;
delete from worker;
```

Limiar volúmenes:

```
docker volume rm timehammer_comunytekdata timehammer_statemachinedata
```

Arrancar el resto de servicios:

```
docker-compose -f docker-compose.runlocal.yml up scheduler
docker-compose -f docker-compose.runlocal.yml up web
docker-compose -f docker-compose.runlocal.yml up core
docker-compose -f docker-compose.runlocal.yml up catalog
docker-compose -f docker-compose.runlocal.yml up integration
docker-compose -f docker-compose.runlocal.yml up comunytek
docker-compose -f docker-compose.runlocal.yml up telegramchatbot
docker-compose -f docker-compose.runlocal.yml up commandprocessor
docker-compose -f docker-compose.runlocal.yml up telegramchatbotnotifier
docker-compose -f docker-compose.runlocal.yml up statemachine
```

Parar todo:

```
docker-compose -f docker-compose.runlocal.yml down
```

# Evitar borrado de imágenes de Docker Hub

Docker Hub ha introducido una nueva política de retención de imágenes para las cuentas gratuitas, bajo la cual las imágenes que no hayan recibido ningún pull/push en 6 meses serán marcadas para ser borradas.

Como solución de contingencia he creado un script que hace un pull de todas las imágenes del proyecto y será ejecutado periódicamente el primer día de cada mes utilizando `crontab`.

Para subir el script al servidor, ejecutar:

```
scp -i %USERPROFILE%/.ssh/timehammer.ovh wksp\server\centos8\pull_images.sh timehammer@54.37.152.149:/home/timehammer/pull_images.sh
```

# HOWTOs

### Cómo recargar la configuración de nginx en caliente (reverseproxy)

Desde dentro del contenedor, ejecutar:

```
s6-svc -h /var/run/s6/services/nginx
```

### Cómo obtener el consumo de CPU y memoria de los contenedores

[https://docs.docker.com/engine/reference/commandline/stats]

```
docker stats --format "table {{.ID}}\t{{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

Para mostrar el resultado ordenado por nombre de contenedor:

```
docker stats --no-stream --format "table {{.ID}}\t{{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}" | (sed -u 1q; sort -k 2)
```

### Cómo mostrar el contenido de un volumen

```
docker run --rm -it -v timehammer_comunytekdata:/vol alpine:latest ls -al /vol
```

### Comandos básicos PSQL

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

### Cómo crear un proyecto quarkus.io

```
$ docker run -it --rm -v C:\work\repos\kronostools\timehammer\wksp\timehammer:/root/wksp/timehammer -v %USERPROFILE%\.m2\repository:/root/.m2/repository maven:3.6.3-jdk-11-slim bash
$ mvn io.quarkus:quarkus-maven-plugin:1.6.0.Final:create \
    -DprojectGroupId=com.kronostools.timehammer \
    -DprojectArtifactId=ssidtracking \
    -DclassName="com.diegocastroviadero.timehammer.ssidtracking.SSIDTrackingResource" \
    -Dpath="/trackSSID"
```

Para arrancar el proyecto quarkus.io en modo de desarrollo

```
$ mvn quarkus:dev
```

# Referencias

### DTO Mappings

https://mapstruct.org/news/2019-12-06-mapstruct-and-quarkus/

### Front - Componente RangeSlider

http://ionden.com/a/plugins/ion.rangeSlider/index.html

### Quarkus - Cheatsheet

https://lordofthejars.github.io/quarkus-cheat-sheet

### Quarkus - Camel

https://camel.apache.org/camel-quarkus/latest/first-steps.html
https://camel.apache.org/camel-quarkus/latest/list-of-camel-quarkus-extensions.html
https://camel.apache.org/components/latest/telegram-component.html

### Telegram - API

https://core.telegram.org/bots/api

### NGINX + Let's encrypt

https://medium.com/@pentacent/nginx-and-lets-encrypt-with-docker-in-less-than-5-minutes-b4b8a60d3a71
https://thepolyglotdeveloper.com/2017/03/nginx-reverse-proxy-containerized-docker-applications

### Docker Compose - extending services

https://docs.docker.com/compose/extends

### Reactive

https://quarkus.io/guides/getting-started-reactive
https://quarkus.io/guides/reactive-routes
https://vertx.io/docs/vertx-web/java
https://quarkus.io/guides/kafka
https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/2/index.html
https://smallrye.io/smallrye-mutiny
https://vertx.io/docs/vertx-pg-client/java

### Hibernate - Relationships

https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/

### Hibernate - Enums

https://vladmihalcea.com/the-best-way-to-map-an-enum-type-with-jpa-and-hibernate/

### Hibernate - HashCode and Equals

https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/
https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
https://vladmihalcea.com/the-best-way-to-map-a-naturalid-business-key-with-jpa-and-hibernate/
https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/

### Hibernate - Date

https://vladmihalcea.com/how-to-store-date-time-and-timestamps-in-utc-time-zone-with-jdbc-and-hibernate/

### Hibernate - Persist collections

https://vladmihalcea.com/merge-entity-collections-jpa-hibernate/

### Hibernate - Validations

https://dwuysan.wordpress.com/2012/03/20/cross-field-validation-using-bean-validation-jsr-303/amp/