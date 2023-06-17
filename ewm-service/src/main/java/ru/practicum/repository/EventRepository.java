package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByInitiator(Long userId);

    @Query(
            "select event from Event as event where event.initiator in :users and event.category in :categories"
    )
    List<Event> findByUsersAndCategories(@Param("users") Long[] users,
                                     @Param("categories") Long[] categories);

    @Query(
            "select event from Event as event where event.initiator in :users"
    )
    List<Event> findByUsers(@Param("users") Long[] users);

    @Query(
            "select event from Event as event where event.category in :category"
    )
    List<Event> findByCategories(@Param("category") Long[] category);

    @Query(
            "select event from Event as event where event.id in :ids"
    )
    List<Event> findByIds(@Param("ids") Long[] ids);

    List<Event> findByCompilation(Long id);

}
