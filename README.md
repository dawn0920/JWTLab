# JWTLab

[바로인턴] 백엔드 개발 과제(java)

Spring Boot 기반 JWT 인증/인가 및 AWS 배포

## 📝 목차

1.  **프로젝트 소개**
2.  **기술 스택**
3.  **실행 방법**
4.  **API 명세**
5.  **GitHub Repository**

-----

## 🚀 프로젝트 소개
이 프로젝트는 Spring Boot를 활용하여 다음 기능을 구현한 백엔드 애플리케이션입니다.

JWT 인증/인가 로직 구현

JUnit을 활용한 테스트 코드 작성

Swagger 기반 API 문서화

AWS EC2를 통한 배포 및 환경 구축

## 🛠️ 기술 스택

  * **주요 기술:**
    * **Spring Boot**
    * **Java**
* **인증/보안:**
    * **Spring Security**
    * **JWT (jjwt)**
* **테스트:**
    * **JUnit 5**
    * **Spring Boot Test**
* **배포/문서화:**
    * **AWS EC2**
    * **Swagger (springdoc-openapi)**



## 🏃 실행 방법

로컬 환경에서 프로젝트를 실행하는 방법.

1.  **프로젝트 클론**

   ```bash
   git clone https://github.com/dawn0920/JWTLab.git
   cd JWtLab 
   ```

2.  **환경 변수 설정**

      * `application.properties` 또는 `application.yml` 파일에 JWT 시크릿 키를 설정해야 합니다.
      * 혹은 IntelliJ 환경변수 설정을 통해 설정 할 수 있습니다.

    ```properties
    # application.properties 예시
    jwt.secret-key=YOUR_SECRET_KEY_HERE
    ```

3.  **애플리케이션 실행**

      * **IntelliJ IDEA**를 사용하여 프로젝트를 열고, 메인 클래스 파일(`*Application.java`)의 `main` 메서드 옆에 있는 `실행 버튼(🟢)`을 클릭하면 됩니다.


-----

제공해주신 Swagger 문서를 기반으로 `README.md`에 포함할 수 있는 **API 명세**를 Markdown 형식으로 정리해 드릴게요. 사용자 API와 관리자 API를 구분하여 가독성을 높였습니다.

-----

## 📃 API 명세

### Swagger UI

**Swagger UI 주소:** [http://54.252.58.9:8080/swagger-ui/index.html\#/](http://54.252.58.9:8080/swagger-ui/index.html#/)

  * Swagger UI를 통해 모든 API 엔드포인트와 요청/응답 형식을 직접 확인하고 테스트할 수 있습니다.

### API 명세

  #### **1. 유저 API**

  * **회원가입**

      * **URL:** `POST /api/auth/signup`
      * **설명:** 새로운 사용자를 등록합니다.
      * **요청 바디 (Request Body):**
        ```json
        {
          "email": "test1@test.com",
          "password": "Password1@",
          "name": "홍길동",
          "userRole": "USER"
        }
        ```
      * **응답 (200 OK):**
        ```json
        {
          "status": 200,
          "code": "B001",
          "message": "요청에 성공하였습니다.",
          "result": {
            "email": "test1@test.com",
            "password": "Password1@",
            "name": "홍길동",
            "userRole": "USER"
          }
        }
        ```
      * **오류 응답 (401 Bad Request):**
        ```json
        {
          "status": 401,
          "code": "U002",
          "message": "유효하지 않은 사용자 이메일입니다."
        }
        ```

  * **로그인**

      * **URL:** `POST /api/auth/login`
      * **설명:** 사용자 로그인을 수행하고 JWT 토큰을 발급합니다.
      * **요청 바디 (Request Body):**
        ```json
        {
          "email": "test1@test.com",
          "password": "Password1@"
        }
        ```
      * **응답 (200 OK):**
        ```json
        {
          "status": 200,
          "code": "B001",
          "message": "요청에 성공하였습니다.",
          "result": {
            "accessToken": "token_value"
          }
        }
        ```
      * **오류 응답 (401 Unauthorized):**
        ```json
        {
          "status": 401,
          "code": "U002",
          "message": "유효하지 않은 사용자 이메일입니다."
        }
        ```
        ```json
        {
          "status": 401,
          "code": "U003",
          "message": "유효하지 않은 사용자 비밀번호입니다."
        }
        ```

#### **2. 관리자 API**

  * **권한 변경**
      * **URL:** `PATCH /api/admin/user/{targetUserId}/roles`
      * **설명:** 관리자 권한을 가진 사용자가 다른 유저의 권한을 변경합니다.
      * **URL 파라미터:** `targetUserId` (변경 대상 사용자의 ID)
      * **응답 (200 OK):**
        ```json
        {
          "status": 200,
          "code": "B001",
          "message": "요청에 성공하였습니다.",
          "result": {
            "email": "test1@test.com",
            "name": "홍길동",
            "userRole": "USER"
          }
        }
        ```
      * **오류 응답 (403 Forbidden):**
        ```json
        {
          "status": 403,
          "code": "ACCESS_DENIED",
          "message": "접근 권한이 없습니다."
        }
        ```
      * **오류 응답 (404 Not Found):**
        ```json
        {
          "status": 404,
          "code": "U001",
          "message": "존재하지 않는 사용자 입니다."
        }
        ```

-----

## 🔗 GitHub Repository

**GitHub Repository 링크:** https://github.com/dawn0920/JWTLab

**AWS EC2 API 엔드포인트:** http://54.252.58.9:8080/

-----

