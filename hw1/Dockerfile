FROM openjdk:11
ARG WORKDIR="/usr/local/bin/SimpleHttpServer"
WORKDIR $WORKDIR
COPY ./service/ru/sibirev/otus/microservice/simple_http_server/SimpleHttpServer.java $WORKDIR
EXPOSE 8000
CMD ["java", "SimpleHttpServer.java"]
