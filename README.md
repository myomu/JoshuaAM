# JoshuaAm (Joshua Attendance Management)
>JoshuaAm(Joshua Attendance Management) 는 수기로 작성하는 출석부를
디지털화하여 회원 명단을 관리하고 출석률을 확인할 수 있는 '출석체크 관리 웹 애플리케이션' 입니다.

- - -

## 프로젝트 목표
### 목적
출석 체크의 목적은 회원들의 출석을 파악하여 동향을 분석하고,
출석률을 이용하여 정회원과 준회원을 구분하는 기준으로 활용된다.

### 요구사항
서면으로 출석을 기록하고 관리할 때, 관리자는 특정 회원의 출석 여부를 한눈에 파악하기 어렵고,
출석률을 통해 회원을 구분해야 할 필요가 있을 때마다 일일이 작업을 진행해야 하는 번거로움이 존재한다.

### 가치
Joshua AM 은 각 회원의 정보와 출석률을 표로 정리하여 관리자의 요구사항을 충족시킬 수 있도록 하였다.
또한, 웹 또는 모바일을 통해 접근성을 향상시킬 수 있다.

- - -

## 프로젝트 기간
- 개발기간 : 2024.02 ~ 2024.10
- 개발인원 : 1명

- - - 

## 기술 스택 & 주요 라이브러리
- BackEnd
  - Spring Boot 3.2.2
  - Java 17
  - JPA
  - JWT
- FrontEnd
  - React 18.2.0
  - redux-toolkit
  - tiptap
  - dayjs
  - mui
  - bootstrap
- DB
  - MariaDB
  - Redis
- 배포 환경
  - AWS **EC2**
  - AWS **RDS**
  - AWS **S3**
  - Docker & Docker Compose
  - Nginx

- - -

## 배포 구조
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/JoshuaAmDemo+%EB%B0%B0%ED%8F%AC+%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.png">

- - -

## ERD
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/Joshua+Am+ERD+-+%EC%B5%9C%EC%A2%85.png">

- - -

## 프로젝트 개요
### 1. 로그인 & 회원가입
> 특정 인증키를 통해서 회원가입을 할 수 있습니다.   
> 로그인은 Spring Security를 통해 인증됩니다. 아이디와 비밀번호가 일치하면 JWT를 사용한 AccessToken과 RefreshToken을
> 서버로부터 받게되어 로그인 상태를 유지합니다.

#### 1-1. 회원가입
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85+%EC%98%81%EC%83%81.gif">

#### 1-2. 로그인
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%EB%A1%9C%EA%B7%B8%EC%9D%B8+%EC%98%81%EC%83%81.gif">


### 2. 메인화면
> 메인화면은 마지막 출석 기록을 기준으로 12 주 간의 기록을 그래프로 나타내고 있으며
> 최근 4 주 동안 1번도 출석하지 않은 회원 이름을 보여줍니다.

<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%EB%A9%94%EC%9D%B8+%ED%99%94%EB%A9%B4(%ED%99%88+%ED%99%94%EB%A9%B4).png">

### 3. 출석
> 날짜 선택을 통해 날짜를 선택할 수 있으며 선택하지 않으면 현재 날짜를 기준으로 저장됩니다.  
> 각 그룹별로 회원들이 표시되고 있으며 회원 이름을 체크하고 저장하여 출석체크를 진행할 수 있습니다.   
> 출석 화면에서는 출석 목록이 보여지며 오른쪽의 수정 버튼을 통해 수정하거나
> 왼쪽의 체크박스를 체크하여 출석을 삭제할 수 있습니다.

#### 3-1. 출석체크
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%EC%B6%9C%EC%84%9D%EC%B2%B4%ED%81%AC+%EC%98%81%EC%83%81.gif">

#### 3-2. 출석 수정, 삭제
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%EC%B6%9C%EC%84%9D+%EC%88%98%EC%A0%95%2C+%EC%82%AD%EC%A0%9C%2C+%EB%AA%A9%EB%A1%9D+%EC%98%81%EC%83%81.gif">


### 4. 회원
> 이름, 생년월일, 성별, 그룹을 입력하여 회원을 추가할 수 있습니다. 또한 회원 수정과 삭제가 가능합니다.   
> 회원 목록에서는 출석 데이터를 기반으로 출석률이 표시됩니다. 상단의 날짜를 선택하여 특정 날짜 사이의 출석률도 확인할 수 있으며 취소를 누르면 초기화 됩니다.

#### 4-1. 회원 추가
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%ED%9A%8C%EC%9B%90+%EC%B6%94%EA%B0%80+%EC%98%81%EC%83%81.gif">

#### 4-2. 회원 수정, 삭제
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%ED%9A%8C%EC%9B%90+%EC%88%98%EC%A0%95%2C+%EC%82%AD%EC%A0%9C%2C+%EB%AA%A9%EB%A1%9D+%EC%98%81%EC%83%81.gif">


### 5. 그룹
> 회원들을 나누는 기준이 되는 그룹을 추가, 수정, 삭제를 할 수 있습니다.

<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%EA%B7%B8%EB%A3%B9+%EC%B6%94%EA%B0%80+%EC%88%98%EC%A0%95+%EC%82%AD%EC%A0%9C+%EC%98%81%EC%83%81.gif">


### 6. 게시판
> 에디터를 통해 게시글을 작성할 수 있으며, 간단한 글 수정과 이미지 추가가 가능합니다. 수정 도중 이미지를 삭제할 수 있고 크기를 조절할 수 있습니다.
> 마찬가지로 게시글 수정과 삭제가 가능하고 게시글의 이미지를 클릭하면 확대 또는 축소가 가능합니다.

#### 6-1. 게시글 추가
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%EA%B2%8C%EC%8B%9C%EA%B8%80+%EC%B6%94%EA%B0%80+%EC%98%81%EC%83%81.gif">

#### 6-2. 게시글 수정, 삭제
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%EA%B2%8C%EC%8B%9C%EA%B8%80+%EC%88%98%EC%A0%95+%EC%82%AD%EC%A0%9C+%EC%98%81%EC%83%81.gif">


### 7. 프로필
> 프로필 화면에서 사용자의 비밀번호, 이름, 이메일을 수정할 수 있습니다.  
> 계정 정보 수정 버튼의 아래의 계정 탈퇴를 클릭하면 계정 탈퇴 또한 가능합니다.

<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/%ED%94%84%EB%A1%9C%ED%95%84+%EC%88%98%EC%A0%95+%EC%98%81%EC%83%81.gif">

- - -

## 개선사항 & 문제해결
### AWS 비용 절감
> AWS에서 IPv4 주소 고갈로 인한 문제가 발생하면서, 프리 티어에서도 IPv4 사용량에 따라 추가 요금이 발생.
이를 해결하기 위해 Route 53과 SSL 인증을 Cloudflare로 대체하고, LoadBalancer를 사용 중지함.
또한, 하나의 EC2 인스턴스에서 React와 Spring Boot 서버를 동시에 운영하기 위해 Nginx를 사용하여 설정을 구성.
### 배포 환경 개선
> Docker 도입 이전에는 EC2에 직접 환경 설정을 일일이 해주어야 했지만, Docker를 도입함으로써 설치 과정을 생략하고 바로 실행할 수 있는 배포 환경을 구축하여 개선.
### 보안 부분 개선
> Refresh Token의 보안을 강화하기 위해 Cookie를 생성할 때 HttpOnly와 Secure 옵션을 추가로 설정하였으며,
요청 시 DeviceId(사용자 식별자)와 User-Agent를 비교하여 Refresh Token의 유효성을 검증하는 방식을 도입.
또한, 텍스트 에디터에서 XSS 공격을 방지하기 위해 dompurify 라이브러리를 사용.
### 출석목록 요청 시간 개선
> 출석 목록 데이터 요청 시, 회원 수와 출석 데이터 증가로 인해 시간이 오래 걸리는 문제를 발견.
DTO 생성 시 DB 요청 빈도를 줄여서 성능을 개선.
> - 기존 api 요청 시 메서드 수행 시간 : 5100 ~ 5200 ms
> - 수정 후 api 요청 시 메서드 수행 시간 : 190 ~ 230 ms
### CORS 문제
> SSR 방식에서 CSR 방식으로 프로젝트가 전환되면서 CORS 문제가 발생하였고,
securityFilterChain에 CORS 관련 필터를 추가하여 문제를 해결.

### 반복 요청 제한
> ...