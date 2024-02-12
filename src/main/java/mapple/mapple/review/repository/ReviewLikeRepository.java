package mapple.mapple.review.repository;

import mapple.mapple.review.entity.ReviewLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Long countByReviewId(long reviewId);

    @EntityGraph(attributePaths = {"review", "user"})
    @Query("select rl from ReviewLike rl " +
            "join fetch rl.user u " +
            "where rl.user.id = :userId")
    Page<ReviewLike> findByUserIdWithReviewAndUser(Pageable pageable, @Param("userId") long userId);
}
