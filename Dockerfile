## 1. Java 이미지를 사용합니다.
#FROM amazoncorretto:17
#
## 2. 작업 디렉토리 설정
#WORKDIR /app
#
## 3. Gradle 빌드 파일과 소스 코드를 복사
#COPY build.gradle settings.gradle /app/
#COPY gradlew /app/
#COPY gradle /app/gradle
#COPY src /app/src
#
## 4. gradlew에 실행 권한 부여
#RUN chmod +x gradlew
#
## 5. Gradle 빌드 시 테스트 생략
#RUN ./gradlew build -x test --no-daemon
#
## 6. 생성된 JAR 파일을 컨테이너로 복사
## ARG JAR_FILE=build/libs/*.jar
## COPY ${JAR_FILE} /app/app.jar
#COPY build/libs/*.jar /app/app.jar
#
## 7. 애플리케이션 실행
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

## 다중 스테이지 빌드 방식

# 1. 빌드 스테이지: 빌드를 위한 Java 이미지
FROM gradle:7.5.1-jdk17 AS build

# 2. 빌드 시 전달될 환경 변수를 ARG로 선언
ARG DB_HOST
ARG DB_PORT
ARG DB_NAME
ARG DB_USERNAME
ARG DB_PASSWORD
ARG AWS_ACCESS_KEY
ARG AWS_SECRET_KEY
ARG AWS_S3_BUCKET
ARG AWS_S3_REGION
ARG SECRET_KEY
ARG CORS_ALLOW_1
ARG CORS_ALLOW_2

# 3. 작업 디렉토리 설정
WORKDIR /app

# 4. Gradle 빌드 파일과 소스 코드를 복사
COPY build.gradle settings.gradle /app/
COPY gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

# 5. gradlew에 실행 권한 부여
RUN chmod +x gradlew

# 6. Gradle 빌드 (테스트 생략)
RUN ./gradlew build -x test --no-daemon

# 7. 실행 스테이지: 실제 실행에 필요한 경량 Java 이미지를 사용
FROM amazoncorretto:17-alpine

# 8. 실행 시 사용할 환경 변수로 설정
ENV DB_HOST=${DB_HOST}
ENV DB_PORT=${DB_PORT}
ENV DB_NAME=${DB_NAME}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
ENV AWS_SECRET_KEY=${AWS_SECRET_KEY}
ENV AWS_S3_BUCKET=${AWS_S3_BUCKET}
ENV AWS_S3_REGION=${AWS_S3_REGION}
ENV SECRET_KEY=${SECRET_KEY}
ENV CORS_ALLOW_1=${CORS_ALLOW_1}
ENV CORS_ALLOW_2=${CORS_ALLOW_2}

# 9. 작업 디렉토리 설정
WORKDIR /app

# 10. 빌드 스테이지에서 생성된 JAR 파일을 실행 스테이지로 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 11. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]