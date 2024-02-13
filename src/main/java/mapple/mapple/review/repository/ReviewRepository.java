package mapple.mapple.review.repository;

import jakarta.persistence.LockModeType;
import mapple.mapple.review.entity.Review;
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
}
