FROM nginx:1.17.10-alpine AS generate_nginx_conf
ARG TIMEHAMMER_HOST
ARG TIMEHAMMER_HTTP_PORT
ARG TIMEHAMMER_HTTPS_PORT
ARG TIMEHAMMER_SERVICE_NAME
ARG TIMEHAMMER_SERVICE_PORT

ENV TIMEHAMMER_HOST ${TIMEHAMMER_HOST}
ENV TIMEHAMMER_HOST ${TIMEHAMMER_HOST}
ENV TIMEHAMMER_HTTP_PORT ${TIMEHAMMER_HTTP_PORT}
ENV TIMEHAMMER_HTTPS_PORT ${TIMEHAMMER_HTTPS_PORT}
ENV TIMEHAMMER_SERVICE_NAME ${TIMEHAMMER_SERVICE_NAME}
ENV TIMEHAMMER_SERVICE_PORT ${TIMEHAMMER_SERVICE_PORT}

WORKDIR /tmp
COPY timehammer.conf.template /tmp/timehammer.conf.template
RUN envsubst '$TIMEHAMMER_HTTP_PORT $TIMEHAMMER_HTTPS_PORT $TIMEHAMMER_HOST $TIMEHAMMER_SERVICE_NAME $TIMEHAMMER_SERVICE_PORT' < /tmp/timehammer.conf.template > /tmp/timehammer.conf

FROM nginx:1.17.10-alpine

COPY --from=generate_nginx_conf /tmp/timehammer.conf /etc/nginx/conf.d/default.conf

RUN mkdir -p /etc/nginx/certbot
COPY certbot/1.3.0/options-ssl-nginx.conf /etc/nginx/certbot/options-ssl-nginx.conf
COPY certbot/1.3.0/ssl-dhparams.pem /etc/nginx/certbot/ssl-dhparams.pem

CMD while :; do sleep 6h & wait ${!}; nginx -s reload; done & nginx -g "daemon off;"