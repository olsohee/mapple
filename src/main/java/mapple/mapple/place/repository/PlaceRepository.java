package mapple.mapple.place.repository;

import mapple.mapple.place.entity.Place;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Override
    @EntityGraph(attributePaths = "user")
    Optional<Place> findById(Long aLong);

    @EntityGraph(attributePaths = "user")
    @Query("select p from Place p " +
            "join fetch p.meeting m " +
            "where m.id = :meetingId")
    List<Place> findByMeetingId(@Param("meetingId") Long meetingId);
}
