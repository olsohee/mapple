package mapple.mapple.place.repository;

import mapple.mapple.place.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @EntityGraph(attributePaths = "user")
    @Query("select p from Place p " +
            "join fetch p.user u " +
            "where p.id = :placeId")
    Optional<Place> findByIdWithUser(@Param("placeId") Long placeId);

    @EntityGraph(attributePaths = "user")
    @Query("select p from Place p " +
            "join fetch p.meeting m " +
            "where m.id = :meetingId")
    Page<Place> findAllByMeetingId(Pageable pageable, @Param("meetingId") long meetingId);
}
