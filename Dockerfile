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

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 빌드 파일과 소스 코드를 복사
COPY build.gradle settings.gradle /app/
COPY gradlew /app/
COPY gradle /app/gradle
COPY src /app/src

# 4. gradlew에 실행 권한 부여
RUN chmod +x gradlew

# 5. Gradle 빌드 (테스트 생략)
RUN ./gradlew build -x test --no-daemon

# 6. 실행 스테이지: 실제 실행에 필요한 경량 Java 이미지를 사용
FROM amazoncorretto:17-alpine

# 7. 작업 디렉토리 설정
WORKDIR /app

# 8. 빌드 스테이지에서 생성된 JAR 파일을 실행 스테이지로 복사
COPY --from=build /app/build/libs/*.jar /app/app.jar

# 9. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]