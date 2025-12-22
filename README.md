# BookPick Backend

BookPick MVP 버전의 백엔드 API 서버입니다. AI 기반 도서 큐레이션 및 추천 서비스를 제공합니다.

## Tech Stack

### Core
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Security 6**
- **Spring Data JPA**
- **MySQL 8.0+**

### Libraries & Tools
- **JWT (JJWT 0.12.6)** - 사용자 인증/인가
- **SpringDoc OpenAPI 2.8.11** - API 문서 자동화 (Swagger UI)
- **Lombok** - 보일러플레이트 코드 제거
- **Gemini AI** - AI 기반 도서 추천 및 큐레이션
- **Gradle** - 빌드 도구
- **Docker** - 컨테이너화 및 배포

## Key Features

### 인증 및 사용자 관리
- JWT 기반 인증/인가 시스템
- 회원가입, 로그인, 로그아웃
- Access Token / Refresh Token 관리

### 도서 큐레이션
- AI 기반 도서 추천 (Gemini API 연동)
- 큐레이션 생성, 조회, 수정, 삭제 (CRUD)
- 커서 기반 페이지네이션
- 좋아요 기능

### 커뮤니티
- 댓글 작성 및 관리
- 사용자 독서 취향 관리

## Project Structure

```
src/main/java/BookPick/mvp
├── domain
│   ├── auth                    # 인증/인가
│   │   ├── controller
│   │   ├── service
│   │   └── dto
│   ├── curation                # 도서 큐레이션
│   │   ├── controller
│   │   ├── service
│   │   ├── repository
│   │   ├── dto
│   │   └── util
│   │       └── gemini          # Gemini AI 연동
│   ├── comment                 # 댓글
│   └── ReadingPreference       # 독서 취향
├── security                    # Security 설정
│   ├── config
│   └── handler
└── global                      # 공통 설정 및 유틸

src/main/resources
├── application.yml             # 공통 설정
├── application-local.yml       # 로컬 환경
├── application-dev.yml         # 개발 환경
└── application-prod.yml        # 운영 환경
```

## Getting Started

### Prerequisites
- JDK 17 이상
- MySQL 8.0+
- Gradle 8.x (Wrapper 포함)

### Environment Setup

1. 환경변수 설정 파일 생성
```bash
cp src/main/resources/.env.example src/main/resources/.env
```

2. `.env` 파일 설정
```properties
# Database
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bookpick?serverTimezone=Asia/Seoul
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# JWT
JWT_SECRET=your-secret-key-here
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=604800000

# Gemini API
GEMINI_API_KEY=your-gemini-api-key
```

### Local Development

```bash
# 의존성 설치 및 빌드
./gradlew clean build

# 애플리케이션 실행 (local 프로파일)
./gradlew bootRun --args='--spring.profiles.active=local'

# 또는
java -jar build/libs/*.jar --spring.profiles.active=local
```

서버는 `http://localhost:8081`에서 실행됩니다.

### Docker

```bash
# 이미지 빌드
docker build -t bookpick-backend .

# 컨테이너 실행
docker run -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=dev \
  --name bookpick-api \
  bookpick-backend
```

## API Documentation

애플리케이션 실행 후 아래 주소에서 API 문서를 확인할 수 있습니다:

- **Swagger UI**: `http://localhost:8081/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8081/v3/api-docs`

### Main Endpoints

#### 인증
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃
- `POST /api/auth/refresh` - 토큰 갱신

#### 큐레이션
- `GET /api/curations` - 큐레이션 목록 조회 (커서 페이지네이션)
- `GET /api/curations/{id}` - 큐레이션 상세 조회
- `POST /api/curations` - 큐레이션 생성
- `PUT /api/curations/{id}` - 큐레이션 수정
- `DELETE /api/curations/{id}` - 큐레이션 삭제
- `POST /api/curations/{id}/like` - 좋아요 토글

#### 독서 취향
- `GET /api/preferences` - 독서 취향 조회
- `POST /api/preferences` - 독서 취향 등록/수정

## Development

### Code Style
- Java 코드 스타일은 Google Java Style Guide 준수
- Lombok 활용하여 보일러플레이트 최소화

### Testing
```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 실행
./gradlew test --tests "BookPick.mvp.domain.auth.*"
```

### Branch Strategy
- `main` - 운영 환경 배포 브랜치
- `develop` - 개발 환경 통합 브랜치
- `features/*` - 기능 개발 브랜치
- `hotfix/*` - 긴급 버그 수정 브랜치

## CI/CD

GitHub Actions를 통한 자동 배포 파이프라인:
- `develop` 브랜치 푸시 시 개발 서버 자동 배포
- PR 생성 시 빌드 및 테스트 자동 실행

## Troubleshooting

### MySQL 연결 오류
```
Caused by: java.sql.SQLException: Access denied for user
```
→ `.env` 파일의 데이터베이스 계정 정보를 확인하세요.

### JWT 관련 오류
```
io.jsonwebtoken.security.WeakKeyException
```
→ `JWT_SECRET` 환경변수가 충분히 긴 키(최소 256bit)인지 확인하세요.

## Contributing

1. 이슈 생성 또는 할당된 이슈 확인
2. Feature 브랜치 생성 (`git checkout -b features/AmazingFeature`)
3. 변경사항 커밋 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 푸시 (`git push origin features/AmazingFeature`)
5. Pull Request 생성

## License

This project is licensed under the MIT License.

## Contact

프로젝트 관련 문의사항은 이슈를 통해 남겨주세요.
