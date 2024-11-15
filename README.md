## 개요
이 프로젝트는 선착순 쿠폰 발행을 중심으로 하여 이벤트 기간 동안 조건을 충족한 사용자에게 선착순으로 쿠폰을 발급하는 기능을 제공합니다.
<img width="948" alt="image" src="https://github.com/user-attachments/assets/e51537fa-eb57-4a8f-9f0d-b0cc74d30132">

### 각 서비스의 역할
- **회원 서비스 (User Service)**: 사용자 인증 및 권한 관리.
- **쿠폰 서비스 (Coupon Service)**: 쿠폰 관리 및 쿠폰 상태 처리.
- **쿠폰 발급 서비스 (Issue Coupon Service)**: 쿠폰 발급 및 검증 처리.
- **캐시 서비스 (Cache Service)**: 쿠폰 정보를 캐시하고 빠른 조회를 지원.

### 주요 기술 스택
- **MSA**: 각각의 서비스는 개별적으로 배포와 스케일링이 가능하며, 필요에 따라 독립적으로 유지보수하고 확장할 수 있도록 설계됐습니다.
- **Kafka**: 쿠폰 지급을 비동기 이벤트 기반으로 처리해 대규모 트래픽 상황에서 데이터베이스 부하를 경감하고 요청 손실을 방지해 안정성을 높였습니다.
- **Redis**: 대규모 트래픽 상황에서 조회 속도를 높이기 위해 캐싱을 활용하고, 동시성 제어가 필요한 경우 원자적 연산을 수행해 일관성을 보장했습니다.
- **Docker & Docker Compose**: 모든 서비스를 컨테이너화해 독립적으로 실행 가능하도록 만들었으며, 서비스 간의 연결 및 관리를 용이하게 구현했습니다.

## 프로젝트 목표
* 높은 트래픽 상황에서도 안정적인 쿠폰 발급 시스템 제공
* 비즈니스 로직을 명확히 분리하여 확장 가능하고 유연한 구조 설계
* Redis와 Kafka를 활용하여 시스템 성능 최적화 및 효율적 서비스 간 통신 처리

## Spring Cloud 기반의 MSA 구성도
<img width="891" alt="image" src="https://github.com/user-attachments/assets/d1c10066-a2d1-41dc-946c-69221908efee">

각 서비스가 독립적으로 실행되며, 통합된 설정 관리와 서비스 디스커버리를 통해 효율적인 서비스 연결을 구현했습니다.

#### 구성 요소
- **Spring Cloud Gateway**: 클라이언트 요청의 진입점으로, API 호출을 적절한 마이크로서비스로 라우팅.
- **Eureka Server**: 서비스 디스커버리 서버로, 마이크로서비스들이 서로를 찾고 통신할 수 있도록 지원.
- **Spring Cloud Config Server**: 중앙에서 설정을 관리하며, 각 서비스에 필요한 설정 정보를 제공.
- **Config Repository**: Git을 통해 Config Server가 참조하는 설정 저장소.
- **Micro Service**: 각 마이크로서비스는 Spring Boot로 구축되었으며, Eureka Client로 등록되고 Config Server로 설정을 관리.

## 쿠폰 발행 Flow
<img width="1082" alt="image" src="https://github.com/user-attachments/assets/aa01ca4b-ea92-40c2-a94e-e7e958de4fb5">

1. **User → IssueCoupon Producer**
- 쿠폰 발급 요청: 사용자가 특정 쿠폰 발급을 요청하는 단계. 이 요청은 IssueCoupon Service의 Producer에 의해 처리.
2. **IssueCoupon Producer → Redis**
- 최대 발급 수량 조회: Redis에서 해당 쿠폰의 최대 발급 가능 수량과 현재 발급된 수량을 조회하여 발급 가능 여부를 판단.
- 중복 발급 여부 조회: Redis에서 해당 사용자가 이미 쿠폰을 발급받았는지 여부를 조회하여 중복 발급을 방지.
3. **IssueCoupon Producer → Kafka**
- 쿠폰 발급 대기 큐 적재: 검증이 완료된 쿠폰 발급 요청은 Kafka 큐에 적재. 이를 통해 발급 요청을 비동기적으로 처리.
4. **Kafka → IssueCoupon Consumer**
- 쿠폰 발급 요청 처리: Kafka 큐에 쌓인 발급 요청을 IssueCoupon Service의 Consumer가 하나씩 처리. 이 단계에서 실제로 쿠폰 발급 작업이 수행.
5. **IssueCoupon Consumer → MySQL**
- 쿠폰 발급 내역 저장: 발급이 완료되면 해당 발급 내역을 MySQL에 저장하여 데이터를 영구적으로 보존.
