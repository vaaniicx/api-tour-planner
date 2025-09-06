package at.fhtw.tourplanner.persistence.repository;

import at.fhtw.tourplanner.persistence.entity.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaTourRepository extends JpaRepository<TourEntity, Long> {

    @Query("""
       SELECT t
       FROM TourEntity t
       LEFT JOIN FETCH t.tourLogs
       """)
    List<TourEntity> findAllWithLogs();

    @Query("""
       SELECT t
       FROM TourEntity t
       LEFT JOIN FETCH t.tourLogs
       WHERE t.id = :id
       """)
    Optional<TourEntity> findByIdWithLogs(@Param("id") long id);
}
