package mapple.mapple.friend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import mapple.mapple.friend.entity.RequestStatus;
import mapple.mapple.friend.entity.Friend;
import mapple.mapple.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Friend> findFriendsByUser(User user) {
        return em.createQuery("select f from Friend f " +
                        "where f.fromUser = :user " +
                        "and f.requestStatus = :requestStatus")
                .setParameter("user", user)
                .setParameter("requestStatus", RequestStatus.ACCEPT)
                .getResultList();
    }
}
