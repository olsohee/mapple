# Mapple (마이 플레이스, 마플)

## 주요 이슈

### Querydsl의 사용

#### 스프링 데이터 JPA만 사용했을 때의 문제점

- 리뷰 글을 조회할 때 해당 글에 대한 접근 권한이 있는지 매번 @Query 어노테이션을 통해 JPQL을 직접 작성해주어야 한다. 따라서 중복된 로직이더라도 매번 JPQL을 작성하고 관리해주어야 한다.
  <img width="975" alt="스크린샷 2024-02-13 오후 4 23 25" src="https://github.com/olsohee/mapple/assets/108605017/d030c4dd-57d5-41cb-81d2-09ba09b408fc">
- 서비스 계층에서는 검색 조건이 있을 때와 없을 때를 구분해서 리포지토리의 메소드를 호출해야 한다.
  <img width="847" alt="스크린샷 2024-02-13 오후 4 20 44" src="https://github.com/olsohee/mapple/assets/108605017/6cc48cea-6f30-4081-9fc6-4f70a2f0f29b">
- JPQL을 직접 작성할 때 오타가 있더라도 컴파일 시점에 발견되지 않는다.

#### Querydsl을 통해 문제 해결

- 인터페이스(ReviewRepository)가 아닌 Querydsl을 사용하기 위한 별도의 클래스를 생성하고, 이곳에서 접근 권한에 대한 로직을 별도의 메소드로 뽑아서 사용할 수 있다.
  <img width="770" alt="스크린샷 2024-02-13 오후 4 30 18" src="https://github.com/olsohee/mapple/assets/108605017/e2018ca5-92eb-48b7-b68b-bd51249c8ffd">
- Querydsl을 사용하여 동적 쿼리를 하나의 메소드로 해결할 수 있다.
  <img width="838" alt="스크린샷 2024-02-13 오후 4 24 49" src="https://github.com/olsohee/mapple/assets/108605017/b6cbc038-098f-476d-8b79-9809e8a47ea6">
- Querydsl을 사용하여 오타가 발생해도 컴파일 시점에 발견된다.

### MySQL의 Full Text Search 사용

- 검색어를 통한 리뷰 글 조회 시 데이터베이스에서 full scan을 하는 문제가 발생했다. 이는 like 연산자가 %_%인 경우 인덱스를 걸어도 인덱스를 타지 않기 때문이다.
- 따라서 MySQL의 Full Text Search를 사용했다.
- 이를 위해서는 match ... against ... 이라는 MySQL에 종속적인 문법을 사용해야 한다. 따라서 네이티브 쿼리로 작성해주었다.
  <img width="956" alt="image" src="https://github.com/olsohee/mapple/assets/108605017/0ddb7e1e-fb03-4462-8ef3-6154056f66a7">
- 따라서 검색어를 통한 조회는 별도의 api로 분리해서 네이티브 쿼리를 실행하도록 하고, 그 외의 조건 검색(좋아요 순, 최신 순으로 조회 등)은 Querydsl을 통해 실행되도록 한다.(Querydsl을
  사용함으로써 접근 권한 검증 로직을 메소드로 분리해서 재사용)
- 이전에 full scan을 타는 조회에서 Full Text Search를 사용하도록 함으로써 조회 성능이 약 52% 향상(1370ms -> 665ms)되었다.
<img width="139" alt="스크린샷 2024-02-15 오후 3 57 19" src="https://github.com/olsohee/mapple/assets/108605017/740e8dcd-c564-4d89-a450-179a5fe0340f">
<img width="141" alt="스크린샷 2024-02-15 오후 3 57 25" src="https://github.com/olsohee/mapple/assets/108605017/e4b4c49b-3ec9-43dd-835f-d87f5c180c58">

### OSIV 옵션과 커멘드와 쿼리의 분리

- OSIV 옵션을 키면 프레젠테이션 계층에서도 영속성 컨텍스트가 살아있게 되고 커넥션이 유지된다.
    - 따라서 커넥션 낭비를 막기 위해 OSIV 옵션을 껐다.
- 대신 트랜잭션 밖에서 영속성 컨텍스트가 닫히므로, 지연로딩이 불가하다.
    - 따라서 트랜잭션 내(서비스 계층)에서 필요한 데이터를 모두 로딩해서 DTO 형태로 반환한다.
- 그런데 이로 인해 서비스 계층에 주요 비즈니스 로직과 DTO 변환 코드(컨트롤러가 필요로 하는 데이터 조회 코드)가 섞이게 된다.
    - 따라서 핵심 비즈니스 로직을 담당하는 서비스 계층(ReviewRepository)와 조회용 서비스 계층(ReviewQueryService)을 분리했다.

### 동시성 상황에서 데이터 정합성 문제

- 동시에 같은 사용자가 좋아요를 누르는 경우와 동시에 여러 사용자가 좋아요를 누르는 경우 데이터 정합설 문제가 발생했다.
- 따라서 비관적 락을 통해 이를 해결했다.
