FROM openjdk:8-alpine
MAINTAINER C. Oliver Godby <oliver@scholanoctis.com>

RUN mkdir /code
RUN mkdir /code/toggler-configs

ADD ./config.json /code/toggler-configs
ADD ./target/toggler-1.1.5-standalone.jar /code
ADD ./docker/run-toggler.sh /code

RUN chmod +x /code/run-toggler.sh

EXPOSE 7000

ENTRYPOINT ["/code/run-toggler.sh"]
