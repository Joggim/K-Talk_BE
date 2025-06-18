# K-Talk Backend

**K-Talk**은 **외국인 한국어 학습자를 위한 음성 인식 및 AI 기반 발음 교정 및 회화 연습 서비스**입니다. K-Talk의 백엔드 서버는 Spring Boot 기반의 REST API를 제공합니다.
> 전체 프로젝트 설명은 [K-Talk 프로젝트 소개 레포지토리](https://github.com/Joggim/team-26-joggim)에서 확인하실 수 있습니다.

<br>

## 📌 주요 기능

- 사용자 인증 및 정보 관리 (Google OAuth2)
- 문장 학습 및 발음 피드백 기록
- 발음 오류 분석 및 유형 분류
- AI 서버와의 통신 (STT, 발음 평가, 추천)
- 사용자 맞춤 추천 학습 경로 제공

<br>

## 🛠️ 기술 스택

| 분류 | 사용 기술                             |
|------|-----------------------------------|
| Language | Java 17                           |
| Framework | Spring Boot 3.4.2                 |
| DB | MySQL                             |
| ORM | Spring Data JPA                   |
| 인증 | Spring Security + OAuth2 (Google) |
| 통신 | RestTemplate (AI 서버 연동)           |
| 문서화 | Swagger (springdoc-openapi)       |
| 빌드 | Gradle                            |

<br>

## 📁 프로젝트 구조

```
src
├── main
│   ├── java/com/joggim/ktalk
│   │   ├── controller        # API 엔드포인트
│   │   ├── service           # 비즈니스 로직
│   │   ├── domain            # 엔티티 클래스
│   │   ├── repository        # JPA 레포지토리
│   │   ├── dto               # 데이터 전송 객체
│   │   ├── config            # 외부 연동 및 설정
│   │   ├── security          # 인증 및 인가 관련 보안 로직
│   │   └── common            # 공통 응답, 예외 처리 등
│   └── resources
│       └── application.properties   # 설정 파일
```

<br>

## ⚙️ 실행 방법 (Docker로 실행)

### 1. `.env` 파일 생성

루트 디렉토리에 `.env` 파일을 생성하고 아래와 같이 환경변수를 설정합니다:

```dotenv
DB_URL=jdbc:mysql://[DB_HOST]:[PORT]/[DB_NAME]
DB_USERNAME=[YOUR_DB_USERNAME]
DB_PASSWORD=[YOUR_DB_PASSWORD]
DB_DRIVER=com.mysql.cj.jdbc.Driver

AWS_S3_BUCKET_NAME=[YOUR_BUCKET]
AWS_ACCESS_KEY=[YOUR_ACCESS_KEY]
AWS_SECRET_KEY=[YOUR_SECRET_KEY]

GOOGLE_CLIENT_ID=[YOUR_GOOGLE_CLIENT_ID]
GOOGLE_CLIENT_SECRET=[YOUR_GOOGLE_CLIENT_SECRET]

AI_SERVER_URL=http://[AI_SERVER_HOST]:[PORT]
```
- `DB_URL` : MySQL 데이터베이스 접속 URL (ex. `jdbc:mysql://localhost:3306/ktalk`)
- `DB_USERNAME` : 데이터베이스 사용자 이름
- `DB_PASSWORD` : 데이터베이스 비밀번호
- `DB_DRIVER` : JDBC 드라이버 클래스 (default: `com.mysql.cj.jdbc.Driver`)
- `AWS_S3_BUCKET_NAME` : AWS S3 버킷 이름
- `AWS_ACCESS_KEY` : AWS 접근 키 (Access Key)
- `AWS_SECRET_KEY` : AWS 비밀 키 (Secret Key)
- `GOOGLE_CLIENT_ID` : Google OAuth2 클라이언트 ID
- `GOOGLE_CLIENT_SECRET` : Google OAuth2 클라이언트 비밀 키
- `AI_SERVER_URL` : 발음 평가용 AI 서버 주소 (ex. `http://localhost:8000`)

필요한 설정은 `application.properties`에서 `.env`를 불러와 사용합니다.

> ⚠️ `AWS_ACCESS_KEY`, `AWS_SECRET_KEY`, `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`은  
> 각자 **AWS 콘솔**과 **Google Cloud Console**에서 직접 발급받아야 합니다.
- AWS: [https://console.aws.amazon.com/iam](https://console.aws.amazon.com/iam)
    - S3 버킷 권한이 포함된 IAM 사용자를 생성하고 키를 발급하세요.
- Google: [https://console.cloud.google.com/apis/credentials](https://console.cloud.google.com/apis/credentials)
    - OAuth 2.0 클라이언트 ID를 생성하고 리디렉션 URI 설정 필요

### 2. JAR 파일 빌드
```bash
./gradlew build
```
> 빌드 완료 후 `build/libs/*.jar` 파일이 생성됩니다.

### 3. Docker 이미지 빌드
```bash
docker build -t ktalk-backend .
```
> Dockerfile은 루트 디렉토리에 있어야 하며, 위에서 생성된 JAR이 존재해야 합니다.

### 4. Docker 컨테이너 실행
```bash
docker run --env-file .env -p 8080:8080 ktalk-backend
```
> 실행 후 `http://localhost:8080`에서 API에 접근할 수 있습니다.

### ⚠️ Docker 실행 주의 사항
- Docker 엔진이 실행 중이어야 함
  - Windows/macOS 사용자는 Docker Desktop 실행 필요
  - Linux 사용자는 sudo systemctl start docker 등으로 도커 데몬 실행
- JAR 파일이 없을 경우 COPY 단계에서 빌드 실패가 발생

<br>

## 🔌 주요 API

| 분류      | 메서드 | 경로                                              | 설명 |
|---------|--------|-------------------------------------------------|------|
| 📘 학습 주제 | GET | `/api/topics`                                   | 학습 주제 목록 조회 |
|         | GET | `/api/topics/{topicId}/sentences`               | 주제별 문장 목록 조회 |
| 📚 문장   | GET | `/api/sentences/{sentenceId}`                   | 문장 상세 조회 |
|         | POST | `/api/sentences/{sentenceId}/feedback`          | 문장 발음 피드백 저장 |
| 🧾 학습 기록 | GET | `/api/user/learning-history`                    | 사용자 학습 이력 조회 |
| 🔊 변환   | POST | `/api/convert/tts`                              | 텍스트 → 음성 변환 (TTS) |
|         | POST | `/api/convert/stt`                              | 음성 → 텍스트 변환 (STT) |
| 💬 채팅   | POST | `/api/chat/reply`                               | AI 채팅 응답 생성 |
|         | POST | `/api/chat/feedback`                            | 채팅 피드백 저장 |
|         | GET | `/api/chat/messages`                            | 채팅 메시지 목록 조회 |
| ❗ 발음 오류 | GET | `/api/pronunciation-issue`                      | 전체 발음 오류 유형 조회 |
|         | GET | `/api/pronunciation-issue/{issueId}`            | 오류 유형 상세 조회 |
|         | GET | `/api/pronunciation-issue/{issueId}/error-logs` | 오류 유형별 사용자 발음 기록 |
> 🔧 전체 API 요청/응답 상세는 Swagger UI(`/swagger-ui/index.html`)에서 확인할 수 있습니다. (서버 실행 시 접근 가능)
