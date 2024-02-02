package mapple.mapple.friend.repository;

import mapple.mapple.friend.entity.Friend;
import mapple.mapple.friend.entity.RequestStatus;
import mapple.mapple.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from Friend f " +
            "where f.fromUser = :user " +
            "and f.requestStatus = :requestStatus")
    List<Friend> findFriendsByUser(@Param("user") User user,
                                   @Param("requestStatus") RequestStatus requestStatus);
}
