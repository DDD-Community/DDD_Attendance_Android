# DDD Attendance Android

DDD 커뮤니티를 위한 출석 관리 Android 애플리케이션입니다. 관리자는 QR 코드 스캔을 통해 출석을 관리하고, 멤버는 자신의 출석 현황을 확인할 수 있습니다.

## 주요 기능

### 멤버 (사용자)
- Google 소셜 로그인
- 출석 현황 조회
- 스케줄/이벤트 확인
- 프로필 관리

### 관리자
- QR 코드 스캔을 통한 출석 체크
- 팀별/일정별 출석 현황 관리
- 출석 상태 변경 (참석/지각/결석)
- 스케줄 관리
- 팀 관리

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin |
| UI | Jetpack Compose, Material3 |
| Architecture | Clean Architecture, MVI |
| DI | Hilt |
| Networking | Retrofit, OkHttp |
| Serialization | Kotlinx Serialization |
| Local Storage | DataStore |
| Authentication | Google Sign-In |
| Camera & QR | CameraX, ZXing |
| Image Loading | Coil |
| Async | Kotlin Coroutines |

## 프로젝트 구조

```
DDD_Attendance_Android/
├── app/                          # Main Application
│
├── domain/                       # Domain Layer
│   ├── model/                    # 도메인 모델
│   ├── repository/               # Repository 인터페이스
│   └── usecase/                  # UseCase (비즈니스 로직)
│
├── data/                         # Data Layer
│   ├── data/                     # Repository 구현체, Mapper
│   ├── data/api/                 # Retrofit API
│   ├── data/google/              # Google 로그인
│   └── data/datastore/           # 로컬 저장소
│
├── feature/                      # Feature Modules (UI Layer)
│   ├── designsystem/             # 공통 UI 컴포넌트
│   ├── core/                     # QR 코드 등 공통 유틸
│   ├── splash/                   # 스플래시 화면
│   ├── home/                     # 홈 화면
│   ├── login/                    # 로그인
│   ├── onboarding/               # 온보딩
│   ├── member/                   # 멤버 기능
│   │   ├── main/                 # 멤버 메인
│   │   ├── attendance/           # 출석 현황
│   │   └── profile/              # 프로필
│   └── admin/                    # 관리자 기능
│       ├── main/                 # 관리자 메인
│       ├── attendance/           # 출석 관리
│       ├── schedule/             # 스케줄 관리
│       └── profile/              # 프로필
│
└── build-logic/                  # Gradle Convention Plugins
```

## 아키텍처

```
┌─────────────────────────────────────────┐
│           Feature Modules (UI)          │
│    Compose + ViewModel + Intent/State   │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Domain Layer                  │
│     UseCase + Repository Interface      │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│            Data Layer                   │
│  Repository Impl + DataSource + Mapper  │
└─────────────────────────────────────────┘
```

## 화면 구성

| 화면 | 설명 |
|------|------|
| Splash | 앱 시작 화면 |
| Login | Google 로그인 |
| Onboarding | 신규 가입 (이메일 인증, 정보 입력) |
| Member Main | 멤버 홈 화면 |
| Member Attendance | 출석 기록 조회 |
| Member Profile | 프로필 관리 |
| Admin Main | 관리자 홈 (팀별 출석 현황) |
| Admin Attendance | QR 스캔 및 출석 관리 |
| Admin Schedule | 스케줄 관리 |
| Admin Profile | 관리자 프로필 |

## 빌드 및 실행

### 요구사항
- Android Studio Ladybug 이상
- JDK 17+
- Android SDK 36

## 모듈 의존성

```
app
├── domain
├── data (api, google, datastore)
└── feature (all modules)

feature/*
├── domain
├── feature/core
└── feature/designsystem

data
└── domain
```

## Convention Plugins

빌드 설정의 일관성을 위해 `build-logic` 모듈에서 Convention Plugin을 사용합니다:

- `attendance.android.application` - Android 앱 설정
- `attendance.android.library` - Android 라이브러리 설정
- `attendance.android.compose` - Jetpack Compose 설정
- `attendance.android.hilt` - Hilt DI 설정
- `attendance.android.retrofit` - Retrofit 설정
- `attendance.android.qrcode` - QR 코드 기능 설정
- `attendance.android.datastore` - DataStore 설정
- `attendance.android.googlelogin` - Google 로그인 설정

## 버전 정보

- Version Name: 1.0.1
- Version Code: 2
- Min SDK: 26
- Target SDK: 36
- Compile SDK: 36

## 라이선스

Copyright 2026 DDD Community
