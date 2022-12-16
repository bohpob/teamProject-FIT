FROM amazoncorretto:17 AS app-build

ENV GRADLE_OPTS -Dorg.gradle.deamon=false

RUN ls -R

COPY . /build
WORKDIR /build

RUN ls -R

RUN chmod +x gradlew

ENTRYPOINT ["java", "-cp", "./src/main/java/cz/cvut/fit/sp/chipin/ChipinApplication.java", "-jar", "build/libs/chipin-0.0.1-SNAPSHOT.jar"]

CMD ./gradlew build