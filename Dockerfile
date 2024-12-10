FROM amazoncorretto:17-alpine AS corretto-deps

COPY ./api/target/*.jar /app/app.jar

RUN unzip /app/app.jar -d temp &&  \
    jdeps  \
      --print-module-deps \
      --ignore-missing-deps \
      --recursive \
      --multi-release 17 \
      --class-path="./temp/BOOT-INF/lib/*" \
      --module-path="./temp/BOOT-INF/lib/*" \
      /app/app.jar > /modules.txt

FROM amazoncorretto:17-alpine AS corretto-jdk

COPY --from=corretto-deps /modules.txt /modules.txt

# hadolint ignore=DL3018,SC2046
RUN apk add --no-cache binutils && \
    jlink \
     --verbose \
     --add-modules "$(cat /modules.txt),jdk.crypto.ec,jdk.crypto.cryptoki" \
     --strip-debug \
     --no-man-pages \
     --no-header-files \
     --compress=2 \
     --output /jre

# hadolint ignore=DL3007
FROM alpine:3.20.3
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY --from=corretto-jdk /jre $JAVA_HOME

EXPOSE 8008
COPY --from=corretto-deps /app/app.jar /app/app.jar
COPY ./api/src/main/resources/application.yml /app/application.yml
WORKDIR /app

CMD ["java", "-cp", ".", "-jar", "app.jar"]
