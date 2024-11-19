# 🎫 선착순 쿠폰 발급 시스템

이 프로젝트는 **이벤트 기간 동안 조건을 충족한 사용자에게 선착순으로 쿠폰을 발급**하는 기능을 중심으로 설계되었습니다.  
**동시성 제어와 대규모 요청 처리**를 기반으로 높은 처리량을 안정적으로 관리하여 빠르고 정확한 쿠폰 발급을 보장하며,  
대규모 트래픽 상황에서도 **서비스 품질과 사용자 경험**을 극대화했습니다.

## 📝 프로젝트 개요
- **기간**: 2024.10.16 ~  
- **인원**: 1인  
- **주요 목표**:  
  - 높은 트래픽 상황에서도 안정적인 쿠폰 발급 시스템 제공
  - 비즈니스 로직을 명확히 분리하여 확장 가능하고 유연한 구조 설계
- [API 명세서](https://www.notion.so/API-12736e800eeb8091a684e1ccd78be70e), [데이터베이스 설계 및 유저플로우](https://github.com/yurim0628/off-coupon/wiki/%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EC%84%A4%EA%B3%84-%EB%B0%8F-%EC%9C%A0%EC%A0%80-%ED%94%8C%EB%A1%9C%EC%9A%B0)

## ⚙️ 기술 스택
<img width="987" alt="image" src="https://github.com/user-attachments/assets/1b4fe537-3342-461a-8b62-d094d629b9b8">

- 기본적으로 **Springboot 3.3** 과 **Java 17** 그리고 **MySQL 8.2**를 기준으로 프로젝트를 구성.
- **Spring Cloud**: MSA 환경에서 서비스 디스커버리, 로드밸런싱 등을 지원하여 서비스 간 통신 및 관리를 효율적으로 수행하기 위해 선택.
- **Redis**: 대규모 트래픽에서 조회 성능을 극대화하기 위한 캐싱을 활용하고, 원자적 명령어를 통해 동시성 제어와 데이터 일관성을 보장하기 위해 선택.
- **Kafka**: 메시지 저장 및 복제로 데이터 손실을 방지하고, 비동기 메시징으로 결합도를 낮추고 트랜잭션 분리로 확장성과 안정성 강화하기 위해 선택.
- **Docker & Docker Compose**: 컨테이너화를 통해 독립적인 환경에서 서비스를 실행하고, 여러 컨테이너를 손쉽게 관리하고 배포하기 위해 선택.

## 🚀 Spring Cloud 기반의 MSA 구성도
![image](https://github.com/user-attachments/assets/a2e8db7d-2a25-4425-bf38-5a882d4b7cb5)

### 주요 구성 요소
- **Spring Cloud Gateway**: 특정 요청에 대해 JWT의 유효성을 검증해 인증된 사용자만 마이크로서비스에 접근할 수 있도록 보안 레이어를 구축.
- **Eureka Server**: 오토 스케일링된 인스턴스의 IP를 게이트웨이가 자동으로 인식해 동적 라우팅과 로드밸런싱을 지원하도록 등록 관리 시스템을 구축.
- **Spring Cloud Config Server**: 중앙 집중식 설정 관리 서버를 구축해 모든 마이크로서비스의 설정을 Git 저장소의 구성 파일로 관리.
- **Redis**: 쿠폰 정보를 캐싱해 빠른 응답을 제공하고 원자적 연산으로 재고 관리의 동시성 제어와 데이터 일관성을 보장.
- **Kafka**: 쿠폰 지급을 비동기 이벤트 기반으로 처리해 대규모 트래픽 상황에서 데이터베이스 부하를 경감하고 요청 손실을 방지해 안정성 보장.
- **Docker & Docker Compose**: 모든 서비스를 컨테이너화해 독립적으로 실행 가능하도록 만들었으며 서비스 간의 연결 및 관리를 용이하게 구현.

### 각 서비스의 주요 역할
- **회원 서비스 (User Service)**:
  - 회원 가입 및 로그인 등 사용자 관리 기능 담당.
 
- **쿠폰 서비스 (Coupon Service)**
  - 쿠폰 등록 및 정보 관리와 관련된 핵심 기능 담당.
  - Write-Around 패턴을 사용해 쿠폰 등록 시 데이터베이스에만 저장하고 캐시는 갱신하지 않음.
  - Cache Miss 시 데이터베이스의 쿠폰 테이블을 조회해 쿠폰 정보를 반환.
  - Write-Back 패턴으로 캐시에 저장된 발행된 쿠폰 수량을 정기적으로 데이터베이스와 동기화.
    
- **쿠폰 발급 서비스 (Issue Coupon Service)**
  - 사용자 요청에 따라 쿠폰 검증 및 발급을 처리하는 핵심 기능 담당.
  - Look-Aside 패턴으로 Cache Miss 시 API 요청으로 쿠폰 정보를 가져온 뒤 비즈니스 로직을 처리.
  - Redis의 Set 자료형을 사용해 쿠폰 재고와 중복 여부를 검증.
  - 검증에 성공하면 Kafka를 통해 쿠폰 발급 이벤트를 다른 서비스로 전달.

## 💡 메인 기능
### 쿠폰 발행 Flow
![image](https://github.com/user-attachments/assets/f62dad5f-b4f1-4faf-9b8e-19677054ef3e)

### 주요 로직
1. **쿠폰 정보 조회**  
   - Look-Aside + Write Around 패턴을 사용하여 처음 조회 시 DB에서 쿠폰 정보를 가져와 Redis에 캐싱하고 이후에는 Redis에서 빠르게 조회.
2. **쿠폰 발급 검증**  
   - Redis의 `SCARD`로 현재 발급된 쿠폰 수를 확인하고, `SISMEMBER`로 사용자가 이미 발급받았는지 확인한 뒤, `SADD`로 사용자 정보를 추가.
   - Lua 스크립트를 활용해 모든 작업을 하나의 원자적 명령으로 처리하며 Redis의 단일 스레드 환경을 이용해 작업 간 충돌과 동시성 문제를 방지.
3. **쿠폰 발급**  
   - 쿠폰 발급 요청 시 쿠폰 발급 수량 및 중복 발급 여부를 검증하여 검증 통과 시 사용자에게 즉시 결과를 반환. 
   - 검증된 요청은 Kafka로 전송되며, Consumer가 병렬로 처리해 DB에 비동기적으로 저장.  
 
## 🎯 캐싱 전략
- [쿠폰 정보에 대한 동기화 전략: Look-Aside + Wirte-Around 패턴](https://github.com/yurim0628/off-coupon/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%BF%A0%ED%8F%B0-%EB%B0%9C%EA%B8%89-%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%EC%84%9C-%EC%BA%90%EC%8B%9C%EC%99%80-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EA%B0%84-%EB%8F%99%EA%B8%B0%ED%99%94-%EC%A0%84%EB%9E%B5#1-%EC%BF%A0%ED%8F%B0-%EC%A0%95%EB%B3%B4%EC%97%90-%EB%8C%80%ED%95%9C-%EB%8F%99%EA%B8%B0%ED%99%94-%EC%A0%84%EB%9E%B5)
- [쿠폰 재고에 대한 동기화 전략: Wirte-Back 패턴](https://github.com/yurim0628/off-coupon/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%BF%A0%ED%8F%B0-%EB%B0%9C%EA%B8%89-%EC%84%9C%EB%B9%84%EC%8A%A4%EC%97%90%EC%84%9C-%EC%BA%90%EC%8B%9C%EC%99%80-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EA%B0%84-%EB%8F%99%EA%B8%B0%ED%99%94-%EC%A0%84%EB%9E%B5#2-%EC%BF%A0%ED%8F%B0-%EC%9E%AC%EA%B3%A0%EC%97%90-%EB%8C%80%ED%95%9C-%EB%8F%99%EA%B8%B0%ED%99%94-%EC%A0%84%EB%9E%B5)

## 🔒 동시성 테스트
- [쿠폰 재고에 대한 동시성 처리 (1): INCR 커맨드](https://github.com/yurim0628/off-coupon/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8%EC%97%90%EC%84%9C-Redis%EC%99%80-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0#1-incr-%EC%BB%A4%EB%A7%A8%EB%93%9C%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%A7%80%EA%B8%89-%EC%9E%AC%EA%B3%A0-%EA%B4%80%EB%A6%AC)
- [쿠폰 재고에 대한 동시성 처리 (2): SET 자료형](https://github.com/yurim0628/off-coupon/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8%EC%97%90%EC%84%9C-Redis%EC%99%80-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0#2-set-%EC%9E%90%EB%A3%8C%ED%98%95%EC%9D%84-%ED%86%B5%ED%95%9C-%EC%A7%80%EA%B8%89-%EC%9E%AC%EA%B3%A0-%EA%B4%80%EB%A6%AC)
- [쿠폰 재고에 대한 동시성 처리 (3): SET 자료형 + Redis Transaction](https://github.com/yurim0628/off-coupon/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8%EC%97%90%EC%84%9C-Redis%EC%99%80-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0#3-set-%EC%9E%90%EB%A3%8C%ED%98%95--redis-transaction-%EC%9D%84-%ED%86%B5%ED%95%9C-%EC%A7%80%EA%B8%89-%EC%9E%AC%EA%B3%A0-%EA%B4%80%EB%A6%AC)
- [쿠폰 재고에 대한 동시성 처리 (4): SET 자료형 + Redis Lua Script](https://github.com/yurim0628/off-coupon/wiki/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8%EC%97%90%EC%84%9C-Redis%EC%99%80-%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88-%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0#4-set-%EC%9E%90%EB%A3%8C%ED%98%95--lua-script%EB%A5%BC-%ED%86%B5%ED%95%9C-%EC%A7%80%EA%B8%89-%EC%9E%AC%EA%B3%A0-%EA%B4%80%EB%A6%AC)

## 🛠️ 분산 아키텍처
[Kafka를 활용해 검증 서비스와 발행 서비스 분리](https://github.com/yurim0628/off-coupon/wiki/%EB%A9%94%EC%8B%9C%EC%A7%80-%ED%81%90%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%B4-%EA%B2%80%EC%A6%9D-%EC%84%9C%EB%B9%84%EC%8A%A4%EC%99%80-%EB%B0%9C%ED%96%89-%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%B6%84%EB%A6%AC%ED%95%98%EA%B8%B0)
