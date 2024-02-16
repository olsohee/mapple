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
public interface ReviewRepository extends ReviewRepositoryCustom, JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = "user")
    @Query("select r from Review r where r.id = :reviewId")
    Optional<Review> findByIdWithUser(@Param("reviewId") Long reviewId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Review r where r.id = :reviewId")
    Review findReviewWithLock(@Param("reviewId") Long reviewId);

    @EntityGraph(attributePaths = "user")
    List<Review> findByUserId(long userId);

    @Query(value = "select r.* from review r " +
            "left join user u on u.user_id = r.user_id " +
            "left join friend f on f.from_user_id = :userId and f.to_user_id = u.user_id " +
            "where match(place_name) against(:keyword in boolean mode) " +
            "and (r.public_status = 'PUBLIC' " + // 전체공개이거나
            "or r.user_id = :userId " + // 유저 자신의 리뷰이거나
            "or (r.public_status = 'ONLY_FRIEND' and f.request_status = 'ACCEPT'))", // 친구의 리뷰
            nativeQuery = true)
    Page<Review> findReviewsPageSearch(@Param("keyword") String keyword, @Param("userId") long userId, Pageable pageable);

    List<Review> findTop5ByOrderByLikeCountDesc();
}
