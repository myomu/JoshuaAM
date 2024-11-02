# JoshuaAm
>JoshuaAm(Joshua Attendance Management)는 수기로 작성하는 출석부를
디지털화하여 회원 명단을 관리하고 출석률을 확인할 수 있는 '출석체크 관리 웹 애플리케이션' 입니다.

- - -

## 프로젝트 목표
### 목적
출석 체크의 목적은 회원들의 출석 현황을 파악하여 동향을 분석하고,
출석률을 기준으로 정회원과 준회원을 구분하는 것입니다.

### 요구사항
서면으로 출석을 기록하고 관리할 때, 관리자는 특정 회원의 출석 여부와 출석률을 즉시 파악하기 어렵습니다.
더불어, 회원 구분을 위해 출석률을 확인할 때마다 전체 회원의 출석률을 일일이 계산해야 하는 번거로움이 있습니다.

### 구현
JoshuaAM은 그룹별로 회원들의 정보를 관리하고 출석체크를 할 수 있습니다.
관리자의 요구사항을 충족시키기 위해 각 회원의 정보와 출석률을 표로 정리했으며,
게시판을 통해 간단한 기록을 남길 수 있는 기능도 추가했습니다.

- - -

## 프로젝트 기간
- 개발기간 : 2024.02 ~ 2024.11
- 개발인원 : 1명

- - -

## 기술 스택 & 주요 라이브러리
- BackEnd
  - Spring Boot 3.2.2
  - Java 17
  - JPA
  - JWT
  - Bucket4j
- FrontEnd
  - React 18.2.0
  - Redux-toolkit
  - Tiptap
  - Dayjs
  - Mui
  - Bootstrap
- DB
  - MariaDB
  - Redis
- 배포 환경
  - AWS **EC2**
  - AWS **RDS**
  - AWS **S3**
  - AWS **Secrets Manager**
  - Docker & Docker Compose
  - Nginx
  - CI/CD
    - GitHub Actions

- - -

## JoshuaAM Front
> JoshuaAM의 프론트엔드 부분 GitHub 저장소 주소는 다음과 같습니다.
> - https://github.com/myomu/JoshuaAMFront

- - -

## 배포 구조
<img src="https://joshuaam-bucket.s3.ap-northeast-2.amazonaws.com/readmefile/JoshuaAmDemo+%EB%B0%B0%ED%8F%AC+%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8ver2.png">

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
![회원가입 영상-min](https://github.com/user-attachments/assets/c2a5e1e7-1308-4694-a323-ff5a9c22f66a)

#### 1-2. 로그인
![로그인 영상-min](https://github.com/user-attachments/assets/bc99557e-292a-4a6a-9d23-9a749d752b95)


### 2. 메인화면
> 메인화면은 마지막 출석 기록을 기준으로 12 주 간의 기록을 그래프로 나타내고 있으며
> 최근 4 주 동안 1번도 출석하지 않은 회원 이름을 보여줍니다.

![메인 화면(홈 화면)](https://github.com/user-attachments/assets/b2c787ec-0c9d-4b3c-9147-88003483da95)


### 3. 출석
> 날짜 선택을 통해 날짜를 선택할 수 있으며 선택하지 않으면 현재 날짜를 기준으로 저장됩니다.  
> 각 그룹별로 회원들이 표시되고 있으며 회원 이름을 체크하고 저장하여 출석체크를 진행할 수 있습니다.   
> 출석 화면에서는 출석 목록이 보여지며 오른쪽의 수정 버튼을 통해 수정하거나
> 왼쪽의 체크박스를 체크하여 출석을 삭제할 수 있습니다.

#### 3-1. 출석체크
![출석체크 영상-min](https://github.com/user-attachments/assets/c3b3c3e5-ad06-4a10-a122-e13cd53975bf)

#### 3-2. 출석 수정, 삭제
![출석 수정, 삭제, 목록 영상-min](https://github.com/user-attachments/assets/1a3d2250-8b95-4807-b0c6-ca9821ee3895)


### 4. 회원
> 이름, 생년월일, 성별, 그룹을 입력하여 회원을 추가할 수 있습니다. 또한 회원 수정과 삭제가 가능합니다.   
> 회원 목록에서는 출석 데이터를 기반으로 출석률이 표시됩니다.
> 상단의 날짜를 선택하여 특정 날짜 사이의 출석률도 확인할 수 있으며 취소를 누르면 초기화 됩니다.

#### 4-1. 회원 추가
![회원 추가 영상-min](https://github.com/user-attachments/assets/e9a4225d-6a8c-4d5e-9156-fe98bcb18ed4)

#### 4-2. 회원 수정, 삭제
![회원 수정, 삭제, 목록 영상-min](https://github.com/user-attachments/assets/4c26d2f5-e4de-407a-b91f-d679bdedb5d8)


### 5. 그룹
> 회원들을 나누는 기준이 되는 그룹을 추가, 수정, 삭제를 할 수 있습니다.

![그룹 추가 수정 삭제 영상-min](https://github.com/user-attachments/assets/90d7f0c1-e01a-4eb5-8d05-be6ad8547d28)


### 6. 게시판
> 에디터를 통해 게시글을 작성할 수 있으며, 간단한 글 수정과 이미지 추가가 가능합니다.
> 수정 도중 이미지를 삭제할 수 있고 크기를 조절할 수 있습니다.
> 마찬가지로 게시글 수정과 삭제가 가능하고 게시글의 이미지를 클릭하면 확대 또는 축소가 가능합니다.

#### 6-1. 게시글 추가
![게시글 추가 영상-min](https://github.com/user-attachments/assets/a06a8d55-80ed-4b03-9ae5-88c18d3b6f81)

#### 6-2. 게시글 수정, 삭제
![게시글 수정 삭제 영상-min](https://github.com/user-attachments/assets/90afe404-dace-4987-bbb0-7b171a375b1c)


### 7. 프로필
> 프로필 화면에서 사용자의 비밀번호, 이름, 이메일을 수정할 수 있습니다.  
> 계정 정보 수정 버튼의 아래의 계정 탈퇴를 클릭하면 계정 탈퇴 또한 가능합니다.

![프로필 수정 영상-min](https://github.com/user-attachments/assets/304b982f-abb0-4f9d-bc83-eec597de5945)

- - -

## 개선사항 & 문제해결
### AWS 비용 절감
- AWS의 IPv4 주소 고갈 문제로 인해 프리 티어에서도 IPv4 사용량에 따른 추가 요금이 발생하게 되었습니다.
- 이 문제를 해결하기 위해 Route 53과 SSL 인증을 Cloudflare로 대체하고, LoadBalancer 사용을 중지했습니다.
또한, 단일 EC2 인스턴스에서 React와 Spring Boot 서버를 동시에 운영하기 위해 Nginx를 활용했습니다.

### 배포 환경 개선
- Docker 도입 이전에는 EC2에 직접 환경 설정을 일일이 해야 했습니다.
- Docker를 도입함으로써 설치 과정을 생략하고, 즉시 실행 가능한 배포 환경을 구축하여 프로세스를 개선했습니다.

### 보안 부분 개선
- Refresh Token의 보안을 강화하기 위해 Cookie 생성 시 HttpOnly와 Secure 옵션을 추가했습니다.
- 요청 시 DeviceId(사용자 식별자)와 User-Agent를 비교하여 Refresh Token의 유효성을 검증하는 방식을 도입했습니다.
- XSS 공격 방지를 위해 텍스트 에디터에 dompurify 라이브러리를 적용했습니다.

### 출석목록 요청 시간 개선
- 출석 목록 데이터 요청 시 DTO를 생성하는 과정에서 DB에 불필요하게 자주 요청하는 문제가 있었습니다.
- 이를 해결하고자 DB 요청 빈도를 줄이는 방식으로 DTO 생성 방법을 변경하여 성능을 크게 개선했습니다.
  - 기존 API 요청 시 메서드 수행 시간: 5,100 ~ 5,200ms
  - 수정 후 API 요청 시 메서드 수행 시간: 190 ~ 230ms

### CORS 문제
- 프로젝트를 SSR 에서 CSR 방식으로 전환하면서 CORS 문제가 발생했습니다.
- 이를 해결하기 위해 CORS 관련 설정 필터를 추가했습니다.
 
### 환경변수 주입 방법 변경
- 기존에는 AWS EC2의 ".env" 파일에서 환경변수를 참조하여 컨테이너에 주입했습니다.
하지만 이 방식은 중요한 값들을 노출시키는 보안 문제가 있었습니다.
대안으로 GitHub Secrets를 시도해 보았으나, 키 값의 가시성이 떨어지고 한 번에 관리하기가 불편했습니다.
- 이러한 문제들을 해결하기 위해 AWS Secrets Manager를 도입했습니다. 이를 통해 외부에서 안전하게 환경변수를 주입할 수 있도록 개선했습니다.

### 반복 요청 제한
- 이전에 React의 interceptor 설정 오류로 무한루프에 빠져 서버에 부하를 주는 일이 발생했습니다. 
또한, 서버 배포 후 Nginx 로그에서 민감한 정보를 요청하는 악의적인 요청이 간혹 발견되었습니다. 예를 들어, 경로를 변경하며 ".env" 파일을 지속적으로 요구하는 요청이 있었습니다.
- 이러한 문제들로 인해 반복되는 요청을 제한할 필요성을 느꼈습니다. 이에 Nginx의 설정과 Spring Boot의 Bucket4j를 사용한 Rate Limiting을 적용했습니다.
