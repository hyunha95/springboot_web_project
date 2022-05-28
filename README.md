# springboot_web_project
spring boot, jpa, thymeleaf, MariaDB   
   
Part01
===
엔티티 클래스와 JpaRepository
---
@Entity   
엔티티 클래스는 Spring Data JPA에서는 반드시 @Entity라는 어노테이션을 추가해야만 한다. @Entity는 해당 클래스가 엔티티를 위한 클래스이며, 해당 클래스의 인스턴스들이 JPA로 관리되는 엔티티 객체라는 것을 의미한다.   
또한 @Entity가 붙은 클래스는 옵션에 따라서 자동으로 테이블을 생성할 수도 있다. 이 경우 @Entity가 있는 클래스의 멤버 변수에 따라서 자동으로 칼럼들도 생성된다.   
   
@Table   
@Entity 어노테이션과 같이 사용할 수 있는 어노테이션으로 말 그대로 데이터베이스상에서 엔티티 클래스를 어떠한 테이블로 생성할 것인지에 대한 정보를 담기 위한 어노테이션이다. 예를 들어 @Table(name="t_memo")와 같이 지정하는 경우에는 생성되는 테이블의 이름이 't_memo' 테이블로 생성된다. 단순히 테이블의 이름뿐만 아니라 인덱스 등을 생성하는 설정도 가능하다.   
   
@Id와 @GeneratedValue   
@Entity가 붙은 클래스는 Primary Key(이하PK)에 해당하는 특정 필드를 @Id로 지정해야만 한다. @Id가 사용자가 입력하는 값을 사용하는 경우가 아니면 자동으로 생성되는 번호를 사용하기 위해서 @GeneratedValue라는 어노테이션을 활용한다.   
@GeneratedValue(strategy = GenerationType.IDENTITY) 부분은 PK를 자동으로 생성하고자 할 때 사용한다(키 생성 전략이라고 한다.). 만일 연결되는 데이터베이스가 오라클이면 별도의 번호를 위한 별도의 테이블을 생성하고, MySQL이나 MariaDB면 'auto increment'를 기본으로 사용해서 새로운 레코드가 기록될 떄 마다 다른 번호를 가질 수 있도록 처리된다.   
키 생성 전략은 다음과 같다.   
- AUTO(default) : JPA 구현체(스프링 부트에서는 Hibernate)가 생성 방식을 결정
- IDENTITY : 사용하는 데이터베이스가 키 생성을 결정 MySQL이난 MariaDB의 경우 auto increment 방식을 이용
- SEQUENCE : 데이터베이스의 sequence를 이용해서 키를 생성. @SequenceGenerator와 같이 사용
- TABLE : 키 생성 전용 테이블을 생성해서 키 생성. @TableGenerator와 함께 사용   
   
@Column   
만일 추가적인 필드(컬럼)가 필요한 경우에도 마찬가지로 어노테이션을 활용한다. 이 때는 @Column을 이용해서 다양한 속성을 지정할 수 있다. 주로 nullable, name, length 들을 이용해서 데이터베이스의 컬럼에 필요한 정보를 제공한다. 속성 중에 columnDefinition을 이용하면 기본값을 지정할 수도 있다.   
https://github.com/hyunha95/springboot_web_project/blob/55a8b2b98df5c44329e247ab12b8bbeeb9f2e05a/ex2/src/main/java/org/zerock/ex2/entity/Memo.java#L7
   
@Builder를 이용하기 위해서는 @AllArgsConstructor와 @NoArgsConstructor를 항상 같이 처리해야 컴파일 에러가 발생하지 않는다.   
   
   
Spring Data JPA를 위한 스프링 부트 설정
---
application.properties 파일   
- spring.jpa.hibernate.ddl-auto: 프로젝트 실행 시에 자동으로 DDL(create, alter, drop 등)을 생성할 것인지를 결정하는 설정이다. 설정값은 create, create-drop, validate가 있다. 예를 들어 create의 경우에는 매번 테이블 생성을 새로 시도한다. 예제에서는 update를 이용해서 변경이 필요한 경우에는 alter로 변경되고, 테이블이 없는 경우에는 create가 되도록 설정하였다.
- spring.jpa.hibernate.format_sql: 실제 JPA의 구현체인 Hibernate가 동작하면서 발생하는 SQL을 포맷팅해서 출력한다. 실행되는 SQL의 가독성을 높여 준다.
- spring.jpa.show-sql: JPA 처리 시에 발생하는 SQL을 보여줄 것인지를 결정한다.   
   
JpaRepository 인터페이스
---
JpaRepository는 인터페이스이고, Spring Data JPA는 이를 상속하는 인터페이스를 선언하는 것만으로도 모든 처리가 끝난다.  
JpaRepository를 사용할 때는 엔티티의 타입 정보와 @Id의 타입을 지정하게 된다. 이처럼 Spring Data JPA는 인터페이스 선언만으로도 자동으로 스프링의 빈(bean)으로 등록된다.
https://github.com/hyunha95/springboot_web_project/blob/55a8b2b98df5c44329e247ab12b8bbeeb9f2e05a/ex2/src/main/java/org/zerock/ex2/repository/MemoRepository.java#L10   
   
CRUD 연습   
- insert 작업: save(엔티티 객체)
- select 작업: findById(키 타입), getOne(키 타입)
- update 작업: save(엔티티 객체)
- delete 작업: deleteById(키 타입), delete(엔티티 객체)   
특이하게도 insert와 update작업에 사용하는 메서드가 동일하게 save()를 이용하는데 이는 JPA의 구현체가 메모리상(Entity Manager라는 존재가 엔티티들을 관리하는 방식)에서 객체를 비교하고 없다면 insert, 존재한다면 update를 동작키시는 방식으로 동작하기 때문이다.   
   
조회 작업 테스트   
findById()의 경우 java.util 패키지의 Optional 타입으로 반환하기 때문에 한번 더 결과가 존재하는지를 체크하는 형태로 작성하게 된다.   
findById()는 실행한 순간에 이미 SQL은 처리가 된다.   
getOne()의 경우는 조금 다른 방식으로 동작하는데 @Transactional 어노테이션이 추가로 필요하다. getOne()의 경우 리턴 값은 해당 객체이지만, 실제 객체가 필요한 순간까지 SQL을 실행하지 않는다.   
   
수정 작업 테스트   
수정 작업은 등록 작업과 동일하게 save()를 이용해서 처리한다. 내부적으로 해당 엔티티의 @Id값이 일치하는지를 확인해서 insert혹은 update작업을 처리한다.   
   
삭제 작업 테스트   
삭제 작업도 위와 동일한 개념이 적용된다. 삭제하려는 번호(mno)의 엔티티 객체가 있는지 먼저 확인하고, 이를 삭제하려고 한다. deleteById()의 리턴 타입은 void이고 만일 해당 데이터가 존재하지 않으면 org.springframework.dao.EmptyResultDataAccessException 예외를 발생한다.   
   
페이징/정렬 처리하기
---
Spring Data JPA에서 페이징 처리와 정렬은 특이하게도 findAll()이라는 메서드를 사용한다. findAll()는 JpaRepository 인터페이스의 상위인 PagingAndSortRepository의 메서드로 파라미터로 전달되는 Pageable이라는 타입의 객체에 의해서 실행되는 쿼리를 결정하게 된다. 단 한 가지 주의할 사항은 리턴 타입을 Page\<T\> 타입으로 지정하는 경우에는 반드시 파라미터를 Pageable 타입을 이용해야 한다는 점이다.   
   
Pageable 인터페이스   
페이지 처리를 위한 가장 중요한 존재는 org.springframework.data.domain.Pageable 인터페이스이다. Pageable인터페이스는 페이지 처리에 필요한 정보를 전달하는 용도의 타입으로, 인터페이스이기 때문에 실제 객체를 생성할 때는 구현체인 org.springframework.data.domain.PageRequests라는 클래스를 사용한다.   
PageRequset 클래스의 생성자는 특이하게도 protected로 선언되어 new를 이용할 수 없다. 객체를 생성하기 위해서는 static한 of()를 이용해서 처리한다. PageRequest 생성자를 보면 page, size, Sort라는 정보를 이용해서 객체를 생성한다.   
static 메서드인 of()의 경우 몇 가지의 형태가 존재하는데 이는 페이지 처리에 필요한 정렬 조건을 같이 지정하기 위해서 이다.   
- of(int page, int size): 0부터 시작하는 페이지 번호와 개수(size), 정렬이 지정되지 않음
- of(int page, int size, Sort.Direction, String...props): 0부터 시작하는 페이지 번호와 개수, 정렬의 방향과 정렬 기준 필드들
- of(int page, int size, Sort sort): 페이지 번호와 개수, 정렬 관련 정보   
   
페이징 처리   
Spring Data JPA를 이용할 때 페이지 처리는 반드시 '0'부터 시작한다는 점을 기억해야만 한다.   
https://github.com/hyunha95/springboot_web_project/blob/55a8b2b98df5c44329e247ab12b8bbeeb9f2e05a/ex2/src/test/java/org/zerock/ex2/repository/MemoRepositoryTests.java#L88   
주의 깊게 볼 부분 중 하나는 리턴 타입이 org.springframework.data.domain.Page라는 점이다. Page 타입이 흥미로운 이유는 단순히 해당 목록만으로 가져오는데 그치지 않고, 실제 페이지 처리에 필요한 전체 데이터의 개수를 가져오는 쿼리 역시 같이 처리하기 때문이다.   
실제 페이지의 데이터를 처리하는 것은 getContent()를 이용해서 List\<엔티티 타입\>으로 처리하거나 Stream\<엔티티 타입\>을 반환하는 get()을 이용할 수 있다.   
   
정렬 조건 추가하기   
페이징 처리를 담당하는 PageRequest에는 정렬과 관련된 org.springframework.data.domain.Sort 타입을 파라미터로 전달할 수 있다. Sort는 한 개 혹은 여러 개의 필드값을 이용해서 순차적 정렬(asc)이나 역순으로 정렬(desc)을 지정할 수 있다.   
https://github.com/hyunha95/springboot_web_project/blob/55a8b2b98df5c44329e247ab12b8bbeeb9f2e05a/ex2/src/test/java/org/zerock/ex2/repository/MemoRepositoryTests.java#L116   
   
쿼리 메서드(Query Methods) 기능과 @Query
---
Spring Data JPA의 경우에는 이러한 처리를 위해서 다음과 같은 방법을 제공한다.   
- 쿼리 메서드: 메서드의 이름 자체가 쿼리의 구문으로 처리되는 기능
- @Query: SQL과 유사하게 엔티티 클래스의 정보를 이용해서 쿼리를 작성하는 기능
- Querysdl 등의 동적 쿼리 처리 기능   
   
쿼리메서드   
쿼리 메서드는 말 그대로 '메서드의 이름 자체가 질의(query)문'이 되는 흥미로운 기능이다. 쿼리 메서드는 주로 'findBy나 getBy'로 시작하고 이후에 필요한 필드 조건이나 And, Or와 같은 키워드를 이용해서 메서드의 이름 자체로 질의 조건을 만들어 낸다.   
- select를 하는 작업이라면 List 타입이나 배열을 이용할 수 있다.
- 파라미터에 Pageable 타입을 넣는 경우에는 무조건 Page\<E\> 타입   
https://github.com/hyunha95/springboot_web_project/blob/55a8b2b98df5c44329e247ab12b8bbeeb9f2e05a/ex2/src/main/java/org/zerock/ex2/repository/MemoRepository.java#L12   
   
deleteBy로 시작하는 삭제 처리   
쿼리 메서드를 이용해서 'deleteBy'로 메서드의 이름을 시작하면 특정한 조건에 맞는 데이터를 삭제하는 것도 가능하다.   
테스트 코드에는 @Transactional과 @Commit이라는 어노테이션을 사용한다. 이것은 deleteBy..인 경우 우선은 'select'문으로 해당 엔티티 객체들을 가져오는 작업과 각 엔티티를 삭제하는 작업이 같이 이루어지기 때문이다. @Commit은 최종 결과를 커밋하기 위해서 사용한다. 이를 적용하지 않으면 테스트 코드의 deleteBy..는 기본적으로 롤백(Rollback) 처리되어서 결과가 반영되지 않는다. deleteBy는 실제 개발에는 많이 사용되지 않는데 그 이유는 SQL을 이용하듯이 한 번에 삭제가 이루어지는 것이 아니라 각 엔티티 객체를 하나씩 삭제하기 때문이다.   
   
@Query 어노테이션   
@Query의 value는 JPQL(Java Persistence Query Language)로 작성하는데 흔히 '객체지향 쿼리'라고 불리는 구문들이다. @Query를 이용해서는 다음과 같은 작업을 실행할 수 있다.   
- 필요한 데이터만 선별적으로 추출하는 기능이 가능
- 데이터베이스에 맞는 순수한 SQL(Native SQL)을 사용하는 기능
- insert, update, delete와 같은 select가 아닌 DML 등을 처리하는 기능(@Modifying과 함께 사용)   
객체지향 쿼리는 테이블 대신에 엔티티 클래스를 이용하고, 테이블의 컬럼 대신에 클래스에 선언된 필드를 이용해서 작성한다.   
   
@Query의 파라미터 바인딩   
- '?1, ?2'와 1부터 시작하는 파라미터의 순서를 이용하는 방식
- ':xxx'와 같이 ':파라미터 이름'을 활용하는 방식
- ':#{ }'과 같이 자바 빈 스타일을 이용하는 방식
```java
@Transactional
@Modifying
@Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);
```
```java
@Transactional
@Modifying
@Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
int updateMemoText(@Param("param") Memo memo);
```
   
Object[] 리턴   
@Query 장점 중의 하나는 쿼리 메서드의 경우에는 엔티티 타입의 데이터만을 추출하지만, @Query를 이용하는 경우에는 현재 필요한 데이터만을 Object[]의 형태로 선별적으로 추출할 수 있다는 점이다.















   
