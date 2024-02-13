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
