package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Request findByEventAndRequester(Long event, Long requester);

    List<Request> findByRequester(Long userId);

    List<Request> findByEvent(Long event);

    @Query(
            "select request from Request as request where request.event = :event and request.id in :requests"
    )
    List<Request> getByEventAndId(@Param("event") Long event, @Param("requests") Long[] requests);
}
