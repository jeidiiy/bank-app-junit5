# JUnit Bank App

## Jpa LocalDateTime 자동 생성 방법
- @EnableJpaAuditing - 메인 클래스
- @EntityListeners(AuditingEntityListener.class) - 엔티티 클래스
```java
@CreatedDate
@Column(nullable = false)
private LocalDateTime createdAt;

@LastModifiedDate
@Column(nullable = false)
private LocalDateTime updatedAt;
```
