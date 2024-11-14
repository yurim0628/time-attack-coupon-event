## 개요
<img width="948" alt="image" src="https://github.com/user-attachments/assets/e51537fa-eb57-4a8f-9f0d-b0cc74d30132">

이 프로젝트는 선착순 쿠폰 발행을 중심으로 하여, 이벤트 기반으로 쿠폰을 관리하는 서비스입니다. 이벤트 기간 동안 조건을 충족한 사용자에게 선착순으로 쿠폰을 발급하는 기능을 제공합니다.

선착순 쿠폰 발급 시스템을 위한 MSA 기반 구조에서 각 서비스는 독립적으로 관리되며, 확장성 있는 아키텍처를 통해 다양한 요구사항에 대응할 수 있도록 설계되었습니다. 
#### 각 서비스의 역할

- 회원 서비스 (User Service): 사용자 인증 및 권한 관리.
- 쿠폰 서비스 (Coupon Service): 쿠폰 관리 및 쿠폰 상태 처리.
- 쿠폰 발급 서비스 (Issue Coupon Service): 쿠폰 발급 및 검증 처리.
- 캐시 서비스 (Cache Service): 쿠폰 정보를 캐시하고 빠른 조회를 지원.

각 서비스는 Spring Boot와 Java 17을 기반으로 독립적으로 구현되었습니다. 서비스 간 통신은 RESTful API와 Kafka 메시지 브로커를 통해 이루어지며, 데이터 조회 성능을 최적화하기 위해 Redis 기반의 캐시 서비스를 도입했습니다. 또한, Docker와 Docker Compose를 사용하여 서비스를 컨테이너화하고, 이를 통해 서비스 관리와 연결을 용이하게 구현했습니다.

## 기술적 목표

## Spring Cloud 기반의 MSA 구성도
<img width="891" alt="image" src="https://github.com/user-attachments/assets/d1c10066-a2d1-41dc-946c-69221908efee">

- Spring Cloud Gateway: 클라이언트 요청의 진입점으로 각 요청을 적절한 마이크로서비스로 라우팅.
- Eureka Server: 마이크로서비스들이 자신을 등록하고 서로의 위치를 찾아 통신할 수 있도록 지원하는 서비스 디스커버리 서버.
- Spring Cloud Config Server: 분산 마이크로서비스의 설정을 중앙에서 관리하며 Config Repository에서 필요한 설정 정보를 제공.
- Config Repository: Config Server가 참조하는 설정 저장소로 Git을 통해 설정 파일을 관리하고 변경된 설정을 각 서비스에 반영.
- Micro Service: user, coupon, issue-coupon, cache 기능을 각각 독립적으로 수행하는 마이크로서비스들.
