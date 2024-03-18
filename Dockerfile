FROM amazoncorretto:17-alpine-jdk

ENV LANG=en_US.UTF-8

RUN jlink \
    --module-path /opt/java/openjdk/jmods \
    --verbose \
    --add-modules java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.crypto.cryptoki,jdk.unsupported \
    --output /opt/jdk-minimal \
    --compress 2 \
    --no-header-files

RUN addgroup -S appuser --gid 10001 && \
    adduser -S appuser -u 10001 -G appuser

ENV JAVA_HOME=/opt/jdk-minimal
ENV APP_PATH=/opt/app/app.jar

ADD /target/*.jar $APP_PATH
RUN chown -R appuser:appuser /opt

USER appuser

ENTRYPOINT $JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_PATH
