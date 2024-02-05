package mapple.mapple.review.repository;

import mapple.mapple.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Override
    @EntityGraph(attributePaths = "user")
    Optional<Review> findById(Long aLong);

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
    Page<Review> findReadableReviews(Pageable pageable, @Param("userId") long userId);

    @EntityGraph(attributePaths = "user")
    @Query(value = "select r from Review r " +
            "join Friend f on f.fromUser.id = :userId " +
            "where (r.publicStatus = mapple.mapple.entity.PublicStatus.PUBLIC " +
            "or r.publicStatus = mapple.mapple.entity.PublicStatus.ONLY_FRIEND) " +
            "and r.user = f.toUser " +
            "and f.requestStatus = mapple.mapple.friend.entity.RequestStatus.ACCEPT")
    Page<Review> findFriendReviewsByUserIdPage(Pageable pageable, @Param("userId") long userId);
}
