FROM certbot/certbot:v1.3.0

COPY entrypoint.sh /entrypoint.sh
RUN chmod u+x /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]