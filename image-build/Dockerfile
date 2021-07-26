FROM docker.artifactory.es.wcorp.carrefour.com/baseimages/openjdk11:1.1.0
MAINTAINER Arquitectura
ENV http_proxy ''
ENV https_proxy ''
ENV HTTP_PROXY ''
ENV HTTPS_PROXY ''
RUN curl $APP_URL -o /deployments/ms-bootified.jar
EXPOSE 8080
USER 1000
