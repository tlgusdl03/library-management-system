# Library Management System

Spring Boot와 Spring Data JPA를 기반으로 만든 도서관 관리 시스템입니다. 회원, 도서, 도서 사본, 대출, 예약, 알림 도메인을 분리하여 도서 등록과 검색, 회원 관리, 도서 대출/반납/연장, 연체 상태 처리의 기본 흐름을 구현하는 것을 목표로 합니다.

현재 프로젝트는 REST Controller보다 도메인 엔티티, Repository, Service 계층과 테스트 코드 중심으로 구성되어 있습니다.

## 프로젝트 개요

이 시스템은 도서관에서 필요한 핵심 업무를 서비스 계층으로 제공하는 백엔드 애플리케이션입니다.

- 회원 가입, 회원 정보 조회/수정, 탈퇴 처리
- 도서 기본 정보 등록 및 검색
- ISBN 기준의 도서 원장과 개별 도서 사본 관리
- 대출 가능한 도서 사본 선택 및 대출 생성
- 반납 시 도서 사본 상태 복구
- 대출 기간 연장
- 스케줄러를 통한 연체 대출 상태 변경
- 예약과 알림 도메인을 위한 엔티티/Repository 기반 마련

## 기술 스택

| 구분 | 내용 |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.0.2 |
| Build Tool | Gradle |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL |
| Test Database | H2 |
| Library | Lombok, JUnit 5 |

## 시스템 구성

```text
src
├── main
│   ├── java/org/tlgusdl03/demo
│   │   ├── dto          # 서비스 요청/응답 DTO
│   │   ├── entities     # JPA 엔티티와 상태 Enum
│   │   ├── repository   # Spring Data JPA Repository
│   │   ├── service      # 비즈니스 로직 인터페이스/구현체
│   │   └── DemoApplication.java
│   └── resources
│       ├── application.properties
│       └── import.sqla
└── test
    ├── java/org/tlgusdl03/demo
    │   └── service      # 서비스 계층 테스트
    └── resources
        └── application.properties
```

## 계층별 역할

### Entity

데이터베이스 테이블과 매핑되는 핵심 도메인 객체입니다.

- `Members`: 회원 정보, 회원 상태, 연체 일수, 로그인 정보를 관리합니다.
- `Books`: ISBN, 제목, 저자 등 도서의 기본 정보를 관리합니다.
- `BookCopies`: 실제 대출 대상이 되는 개별 도서 사본과 사본 상태를 관리합니다.
- `Loans`: 대출일, 반납 예정일, 대출 상태, 연장 횟수, 회원/도서/사본 식별자를 관리합니다.
- `Reservations`: 예약 순번, 도서, 회원 정보를 관리하기 위한 엔티티입니다.
- `Notifications`: 대출/예약 관련 알림 정보를 관리하기 위한 엔티티입니다.

### Repository

Spring Data JPA 기반의 데이터 접근 계층입니다.

- `MembersRepository`: 회원 CRUD
- `BooksRepository`: ISBN, 제목, 저자 기반 도서 검색
- `BookCopiesRepository`: 도서별 사본 목록 조회, 대출 가능 사본 조회
- `LoansRepository`: 특정 상태와 반납 예정일 기준 대출 목록 조회
- `ReservationsRepository`: 예약 정보 CRUD
- `NotificationsRepository`: 알림 정보 CRUD

### Service

비즈니스 규칙을 담당하는 계층입니다.

- `MemberService`: 회원 가입, 수정, 조회, 탈퇴 처리
- `BookService`: 도서 등록, ID/ISBN/제목/저자 기반 검색
- `LoanService`: 대출, 반납, 연장, 연체 처리

## 주요 기능 명세

### 1. 회원 관리

회원 도메인은 `Members` 엔티티와 `MemberService`를 중심으로 동작합니다.

- 회원 가입
  - `MemberJoinRequest`를 받아 회원을 생성합니다.
  - 신규 회원의 기본 상태는 `normal`입니다.
  - 신규 회원의 기본 연체 일수는 `0`입니다.
- 회원 조회
  - 회원 ID로 회원을 조회합니다.
  - `MemberResponse`로 이름, 전화번호, 상태, 연체 일수, 사용자명을 반환합니다.
- 회원 정보 수정
  - 이름, 주민등록번호, 전화번호, 사용자명, 비밀번호를 수정합니다.
- 회원 탈퇴
  - 데이터를 즉시 삭제하지 않고 회원 상태를 `withdrawn`으로 변경합니다.

### 2. 도서 관리

도서는 도서 기본 정보(`Books`)와 개별 사본(`BookCopies`)으로 나누어 관리합니다.

- 도서 등록
  - ISBN이 처음 등록되는 경우 `books` 테이블에 도서 기본 정보를 저장합니다.
  - 동일 ISBN이 이미 존재하면 도서 기본 정보는 중복 저장하지 않고 사본만 추가합니다.
  - 사본은 `book_copies` 테이블에 상태와 함께 저장됩니다.
- 도서 검색
  - 도서 ID로 검색
  - ISBN으로 검색
  - 제목으로 검색
  - 저자로 검색
- 대출 가능 여부 계산
  - 도서에 연결된 사본 중 하나라도 `available` 상태이면 대출 가능 도서로 응답합니다.

### 3. 대출 관리

대출은 `LoanService`에서 처리하며, 대출 가능한 사본을 찾아 `Loans` 데이터를 생성합니다.

- 도서 대출
  - 요청 정보: 회원 ID, 도서 ID
  - 대출 가능한 사본(`available`)을 하나 선택합니다.
  - 선택된 사본 상태를 `loaned`로 변경합니다.
  - 대출일은 현재 시각, 반납 예정일은 현재 시각 기준 7일 후로 설정합니다.
  - 대출 상태는 `loaned`, 연장 횟수와 연체 일수는 `0`으로 시작합니다.
- 도서 반납
  - 대출 상태를 `returned`로 변경합니다.
  - 연결된 도서 사본 상태를 `available`로 되돌립니다.
- 대출 연장
  - 반납 예정일을 7일 연장하고 연장 횟수를 증가시키는 구조입니다.
- 연체 처리
  - 매일 01:00에 스케줄러가 실행됩니다.
  - 반납 예정일이 지난 `loaned` 상태의 대출을 `overdue` 상태로 변경합니다.

### 4. 예약 및 알림

예약과 알림은 엔티티와 Repository가 준비되어 있으며, 서비스 로직은 추후 확장 대상입니다.

- 예약
  - `Reservations` 엔티티에서 예약 순번, 도서 ID, 회원 ID를 관리합니다.
- 알림
  - `Notifications` 엔티티에서 알림 유형, 대상 ID, 내용, 회원 ID를 관리합니다.
  - 알림 유형은 `loan`, `reservation`으로 구분됩니다.

## 상태 값

### 도서 사본 상태

| 상태 | 의미 |
| --- | --- |
| `available` | 대출 가능 |
| `loaned` | 대출 중 |
| `reserved` | 예약됨 |
| `maintenance` | 점검/수리 중 |
| `discarded` | 폐기됨 |

### 대출 상태

| 상태 | 의미 |
| --- | --- |
| `loaned` | 대출 중 |
| `overdue` | 연체 |
| `returned` | 반납 완료 |

### 회원 상태

| 상태 | 의미 |
| --- | --- |
| `normal` | 정상 회원 |
| `overdue` | 연체 회원 |
| `suspended` | 이용 정지 회원 |
| `withdrawn` | 탈퇴 회원 |

## 데이터베이스 설정

운영/개발 실행 환경은 MySQL을 사용하도록 설정되어 있습니다.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db1?serverTimezone=Asia/Seoul&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=user1
spring.datasource.password=qwer1234
spring.jpa.hibernate.ddl-auto=create-drop
```

테스트 환경은 H2 인메모리 데이터베이스를 사용합니다.

```properties
spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;NON_KEYWORDS=USER
spring.jpa.hibernate.ddl-auto=create-drop
```

## 실행 방법

### 사전 준비

- Java 21
- MySQL 8.x 이상 권장
- `db1` 데이터베이스
- MySQL 사용자 `user1` 및 비밀번호 `qwer1234`

### 애플리케이션 실행

Windows:

```bash
gradlew.bat bootRun
```

macOS/Linux:

```bash
./gradlew bootRun
```

### 테스트 실행

Windows:

```bash
gradlew.bat test
```

macOS/Linux:

```bash
./gradlew test
```

## 테스트 구성

서비스 계층 중심의 테스트가 작성되어 있습니다.

- `MemberServiceTest`: 회원 가입, 수정, 조회, 탈퇴 테스트
- `BookServiceTest`: 도서 등록, ISBN/제목/저자 검색 테스트
- `LoanServiceTest`: 대출/반납/연장 테스트 작성 예정 상태

## 현재 구현 상태 및 참고 사항

- 현재 프로젝트에는 REST API Controller가 없습니다. 외부 HTTP 요청을 받으려면 Controller 계층을 추가해야 합니다.
- `Reservations`, `Notifications`는 엔티티와 Repository는 존재하지만 서비스 기능은 아직 구현되지 않았습니다.
- 대출 기간 연장 로직은 `Loans.extendLoan()`에 정의되어 있으며, 반납 예정일 7일 연장과 연장 횟수 증가를 의도합니다.
- `import.sqla`는 초기 데이터 삽입용 파일로 보이나, 현재 파일명과 일부 컬럼/Enum 값은 실제 엔티티 정의와 맞지 않을 수 있어 사용 전 점검이 필요합니다.
- 운영 설정의 `ddl-auto=create-drop`은 애플리케이션 종료 시 테이블이 삭제될 수 있으므로 실제 운영 환경에서는 `validate` 또는 `update` 등으로 변경하는 것이 좋습니다.

## 향후 개선 사항

- REST Controller 추가 및 API 명세 작성
- 회원 인증/인가 기능 추가
- 예약 서비스 구현
- 알림 발송 서비스 구현
- 연체 일수 계산 및 회원 상태 자동 변경
- 대출 연장 횟수 제한 정책 추가
- 도서 사본과 도서 원장 간 관계 매핑 정비
- 예외 응답 공통 처리
- DTO 입력값 검증 추가
- 테스트 코드 보강 및 테스트 데이터 정리

