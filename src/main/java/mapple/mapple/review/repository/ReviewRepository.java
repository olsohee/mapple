package mapple.mapple.review.repository;

import mapple.mapple.entity.PublicStatus;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.entity.RequestStatus;
import mapple.mapple.review.entity.Review;
import mapple.mapple.user.entity.User;
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
    List<Review> findByPublicStatus(PublicStatus publicStatus);

    @EntityGraph(attributePaths = "user")
    @Query("select r from Review r " +
            "join Friend f on f.fromUser.id = :userId " +
            "where r.user = f.toUser " +
            "and r.publicStatus = :publicStatus " +
            "and f.requestStatus = :requestStatus")
    List<Review> findFriendReviewsByUserId(@Param("userId") long userId,
                                           @Param("publicStatus") PublicStatus publicStatus,
                                           @Param("requestStatus") RequestStatus requestStatus);
}
