# SpartaLogistics

---
## 대규모 AI 시스템 설계 프로젝트!
![sparta](https://github.com/user-attachments/assets/d0edbaa3-4567-4ae8-9897-02dc74e6b2a3)

<br/>
<br/>

# 0. Getting Started (시작하기)
```bash
1. docker-compose 실행

  docker-compose up -d

2. eureka-server 실행 
3. auth, gateway 실행 후 각 service-application 실행
```

<br/>
<br/>

# 1. Project Overview (프로젝트 개요)
- 프로젝트 이름: SpartaLogistics
- 프로젝트 설명: 이번 프로젝트는 Eureka Neflix 를 통한 7개의 애플리케이션이 통합된 MSA(Microservices Architecture) 환경과 멀티 모듈 구조를 갖춘 물류 관리 및 배송 시스템입니다. 이 시스템은 PostgreSQL, Grafana, Zipkin, Redis, Kafka를 포함한 최신 데이터베이스 및 모니터링 도구를 활용했습니다.
<br/>
<br/>

# 2. Team Members (팀원 및 팀 소개)
| 박진우 | 유희진 | 고혁진 |
|:------:|:------:|:------:|
| <img src="https://github.com/user-attachments/assets/c1c2b1e3-656d-4712-98ab-a15e91efa2da" alt="박진우" width="150"> | <img src="https://github.com/user-attachments/assets/78ec4937-81bb-4637-975d-631eb3c4601e" alt="유희진" width="150"> | <img src="https://github.com/user-attachments/assets/78ce1062-80a0-4edb-bf6b-5efac9dd992e" alt="고혁진" width="150"> |
| BE | BE | BE |
| [GitHub](https://github.com/ParkJinWu) | [GitHub](https://github.com/heejin1023) | [GitHub](https://github.com/Hyukjin-Ko) |

<br/>
<br/>

# 3. Key Features (주요 기능)
- **회원가입**:
  - 회원가입 시 DB에 유저정보가 등록됩니다.

- **로그인**:
  - 사용자 인증 정보를 통해 로그인합니다. JWT 토큰 발급

- **게이트웨이 인증**:
  - 모든 서비스를 이용 할 때 로그인시 발급 된 토큰을 통해 인증을 하고 서비스를 이용 할 수 있다.
  - 각 서비스에서는 User의 Role을 통해 인가하여 API 실행이 가능하다.

- **AI**:
  - 각 서비스에서 호출하여 사용 할 수 있는 AI 생성 (각 엔티티의 특성에 맞게 사용 가능)
  - AI엔티티에는 어느 서비스에서 이용했는지 기록

- **주문시스템**:
  - 주문 생성 시 연계되는 주문상품, 배송정보, 배송경로기록이 함께 저장됨
  - 상품과 연계하여 상품 삭제 시 해당 상품이 포함된 주문건도 동시에 삭제 처리함. (기존 상품 수량 정보 원복)
  - 주문 조회 시 해당 주문과 연계된 주문상품, 배송정보, 배송경로기록을 한번에 조회

- **허브 이동 경로**:
  - 출발 허브 ID와 도착 허브 ID를 통해 허브 간 이동경로를 리스트로 반환
  - 허브 간 이동 경로 리스트를 AI에게 요청하여 최적의 이동 경로를 응답

<br/>
<br/>


# 4. Tasks & Responsibilities (작업 및 역할 분담)
|  |  |  |
|-----------------|-----------------|-----------------|
| 박진우    |  <img src="https://github.com/user-attachments/assets/c1c2b1e3-656d-4712-98ab-a15e91efa2da" alt="박진우" width="100"> | <ul><li>깃허브 세팅</li><li>허브, 허브 이동 경로, 상품, 업체</li><li>Kafka 구성</li><li>docker 세팅</li></ul>     |
| 유희진   |  <img src="https://github.com/user-attachments/assets/78ec4937-81bb-4637-975d-631eb3c4601e" alt="유희진" width="100">| <ul><li>멀티모듈구성</li><li>공통 파일 작성 및 관리</li><li>주문, 주문상품, 배송, 배송경로기록</li></ul> |
| 고혁진   |  <img src="https://github.com/user-attachments/assets/78ce1062-80a0-4edb-bf6b-5efac9dd992e" alt="고혁진" width="100">    |<ul><li>Auth(로그인/회원가입)</li><li>게이트웨이를 통한 인증인가</li><li>게이트웨이에서 Swagger 통합</li><li>유저 배송담당자 메시지</li></ul>  |


<br/>
<br/>

# 5. Technology Stack (기술 스택)
## 5.1 Language


<br/>

## 5.2 Backend
|  |  |  |
|-----------------|-----------------|-----------------|
| Spring Boot    |  ![image](https://github.com/user-attachments/assets/6090a57a-94f4-4698-a0a0-63c2051982f8)    | 3.3.3    |

<br/>

## 5.4 Cooperation
|  |  |
|-----------------|-----------------|
| Git    |  <img src="https://github.com/user-attachments/assets/183d0b53-14eb-4a20-8e2a-9d31c13da077" alt="git" width="150" height="150">    |
| Slack    |  <img src="https://github.com/user-attachments/assets/898ed9b4-7a19-4b42-baac-9f8f757b13e5" alt="이미지 설명" width="150" height="150">  |
| Notion    |  <img src="https://github.com/user-attachments/assets/34141eb9-deca-416a-a83f-ff9543cc2f9a" alt="Notion" width="150" height="150">    |

<br/>

# 6. Project Structure (프로젝트 구조)
```plaintext
SpartaLogistics
├── com.sparta.logistics.eureka.server
├── com.sparta.logistics.common
├── com.sparta.logistics.client.ai
├── com.sparta.logistics.client.auth
├── com.sparta.logistics.client.gateway
├── com.sparta.logistics.client.hub
├── com.sparta.logistics.client.order
└── com.sparta.logistics.client.user
                            └── client
                            ├── common
                            │   ├── config
                            │   └── exception
                            ├── controller
                            ├── dto
                            ├── model
                            │   └── validation
                            ├── repository
                            └── service
```

<br/>
<br/>

# 7. Development Workflow (개발 워크플로우)
## 브랜치 전략 (Branch Strategy)
![image](https://github.com/user-attachments/assets/09f6dd59-702f-4990-9112-8e7c4294fc48)

우리의 브랜치 전략은 Git Flow를 기반으로 하며, 다음과 같은 브랜치를 사용합니다.

- Main Branch
  - 배포 가능한 상태의 코드를 유지합니다.
  - 모든 배포는 추후에 이 브랜치에서 이루어집니다.

- Dev Branch
  - 개발 브랜치
  - 개발 중인 모든 기능과 수정 사항이 모이는 브랜치입니다.
  
- Feature Branch
  - 기능 브랜치
  - 새로운 기능이나 버그 수정을 위한 브랜치입니다.

<br/>
<br/>

# 8. Coding Convention
## 문장 종료
```
// 세미콜론(;)
log.info("Hello World!");
```

<br/>


## 명명 규칙
* 변수 & 메소드 : 카멜케이스
```
변수의 와 메소드의 경우 카멜 케이스를쓴다. 
```

<br/>


## 파일 네이밍
```
// 파스칼 케이스
PascalCase
```

<br/>
<br/>

# 9. 커밋 컨벤션
## 기본 구조
```
type : subject

body 
```

<br/>

## type 종류
```
> 제목에는 커밋이 무엇을, 왜 수정했는 지 요약해서 작성
> 
- feat: 새로운 feature
- fix: 버그 등 수정
- docs: 문서 내용 변경
- style: 포맷, 세미콜론 수정 등 코드가 아닌 스타일에 관련된 수정
- refactor: 리팩토링 코드
- test: 테스트 코드 추가 및 리팩토링 테스트 등
- chore: build task 수정, 프로젝트 매니저 설정 수정 등
```

<br/>


## 커밋 예시
```
Feat : 메인 페이지 아이콘 svg으로 교체

완성도를 높이기 위해 기존 sprit 이미지로 작업했던 아이콘들을 svg로 교체

Issues: #11
```

<br/>
<br/>
# 10. 인프라 설계서

![infra](https://github.com/user-attachments/assets/d7c6f95a-df3b-4eba-a491-7225110a6cbc)

<br/>
<br/>
# 11. ERD

https://www.erdcloud.com/d/C57y6a4WafFi3hxF8
<img width="991" alt="스크린샷 2024-09-20 오전 12 57 59" src="https://github.com/user-attachments/assets/31a77e54-0aad-4ad6-b271-063d7a007764">

<br/>
<br/>
# 12. API 명세

https://www.notion.so/bb92f6b6a3fb4455be7cdd9a46e97e73?v=fffa9a8fb7cf81cb958a000c96e2b747&pvs=4

