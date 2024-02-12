package mapple.mapple.review.repository;

import jakarta.persistence.LockModeType;
import mapple.mapple.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = "user")
    @Query("select r from Review r where r.id = :reviewId")
    Optional<Review> findByIdWithUser(@Param("reviewId") Long reviewId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Review r where r.id = :reviewId")
    Review findReviewWithLock(@Param("reviewId") Long reviewId);

    @EntityGraph(attributePaths = "user")
    List<Review> findByUserId(long userId);

    @EntityGraph(attributePaths = "user")
    @Query("select r from Review r " +
            "left outer join Friend f on f.fromUser.id = :userId " +
            "where r.publicStatus = mapple.mapple.entity.PublicStatus.PUBLIC " + // 전체 공개이거나
            "or r.user.id = :userId " + // 유저 자신의 리뷰이거나
            "or (r.user = f.toUser " + // 유저의 친구 리뷰이거나 (친구공개)
                "and f.requestStatus = mapple.mapple.friend.entity.RequestStatus.ACCEPT " +
                "and r.publicStatus = mapple.mapple.entity.PublicStatus.ONLY_FRIEND)")
    Page<Review> findReviews(Pageable pageable, @Param("userId") long userId);

    @EntityGraph(attributePaths = "user")
    @Query("select r from Review r " +
            "left outer join Friend f on f.fromUser.id = :userId " +
            "where r.placeName like %:keyword% " +
            "and (r.publicStatus = mapple.mapple.entity.PublicStatus.PUBLIC " + // 전체 공개이거나
            "or r.user.id = :userId " + // 유저 자신의 리뷰이거나
            "or (r.user = f.toUser " + // 유저의 친구 리뷰이거나 (친구공개)
            "and f.requestStatus = mapple.mapple.friend.entity.RequestStatus.ACCEPT " +
            "and r.publicStatus = mapple.mapple.entity.PublicStatus.ONLY_FRIEND))")
    Page<Review> findReviewsWithKeyword(Pageable pageable, @Param("userId") long userId, @Param("keyword") String keyword);

    @EntityGraph(attributePaths = "user")
    @Query(value = "select r from Review r " +
            "join Friend f on f.fromUser.id = :userId " +
            "where (r.publicStatus = mapple.mapple.entity.PublicStatus.PUBLIC " +
            "or r.publicStatus = mapple.mapple.entity.PublicStatus.ONLY_FRIEND) " +
            "and r.user = f.toUser " +
            "and f.requestStatus = mapple.mapple.friend.entity.RequestStatus.ACCEPT")
    Page<Review> findFriendReviewsByUserIdPage(Pageable pageable, @Param("userId") long userId);
}
