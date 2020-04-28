# TODO

- Llevar servicio /auth/mock/chatbotRegistration a /demo
- Cambiar el ComunytekMockedClient para llevar la cuenta de la contraseña
    - Si el usuario no existe se establece la contraseña
    - Si el usuario existe la contraseña tiene que coincidir
    - Es posible añadir servicio en /demo para cambiar la contraseña del ComunytekMockedClient???
- Gestionar el caso en el que se ha cambiado la contraseña en la empresa y no en el chatbot y todo va a petar
    - Controlar los petes
    - Notificar al usuario de alguna manera
- Elegir el cliente en función de la empresa
- Revisar idea de
    - Acciones clockin / clockout
    - Ámbitos work / lunch
    - Ejecutar acción en ámbito
        - Supone un cambio de estado
        - Acción en Comunytek (traductor de acciones con distintas implementaciones)
    - Estado INIT, WORKING, LUNCHING, ENDED
    - Acciones Comunitek Entrada, Pausar (Motivo), Reanudar, Salida 
- Revisar Chatbot
    - Formato de mensajes
    - Contenido de mensajes
- Despliegue en producción
- Probar en producción que funciona después de incorporar el servicio TimeMachineService
- Hacer repo privado
- *************** DEMO ***************
- Meter test unitarios
    - Probar las validaciones con tests unitarios
- Revisar versión de camel (ya está camel-quarkus-telegram 1.0.0-M6)
- Modularizar ?
- Icono TimeHammer
- Icono KronosTools
- Añadir servicio de estadísticas de llamadas a Comunytek (usar VertX)
- Añadir la posibilidad de meter excepciones al horario
    - cambiar widget
    - la excepcion tendrá un periodo de aplicación
    - en las preferencias añadir una lista de timetable exceptions
    - al recuperar el trabajador con las preferencias se recuperarán únicamente las que apliquen a la fecha actual
- Mejoras sobre validación de registro
    - en el front no se muestrar errores sobre el horario
    - tener en cuenta las validaciones sobre los periodos de excepción
        - que no se solapen
        - que las fechas sean coherentes
- Página para actualizar el perfil de un usuario
    - Similar al login, se recibe un chatId y en base al chatId se recupera el registrationId y se devuelve una URL con el registrationId
    - Se crea una página html donde se carga el perfil actual del usuario y se procesa el envío
    - Actualizar el chatbot para que con el comando /settings lleve a la página del perfil del usuario
- Crear pagina de administración (ej: reset de contraseñas de usuario, revisar mensajes basura)
    - Protegida con el perfil
    - Habrá comando /admin (no público) para acceder a la administración
    - El comando /admin genera un link incluyendo el registrationId
    - Al acceder al Admin con el registrationId se comprueba el perfil del usuario
    - Si el usuario no es admin -> UnauthorizedException
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

---

# Creación de un proyecto quarkus.io

```
$ mvn io.quarkus:quarkus-maven-plugin:1.2.1.Final:create \
    -DprojectGroupId=com.diegocastroviadero.timehammer \
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
# run timehammerdev service
$ docker-compose run --service-ports timehammerdev

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

```
> docker-compose run timehammernativebuild
```

Para compilar en nativo

```
$ mvn clean package -Pnative -Dquarkus.native.container-build=docker

# SI EL CONTENEDOR NO VE EL DE BDD
$ mvn clean package -Pnative -DskipTests -Dquarkus.native.container-build=docker
```

Para construir imagen con el ejecutable nativo

```
> docker-compose up timehammernative
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