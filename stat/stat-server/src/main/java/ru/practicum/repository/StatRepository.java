package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(
            "select new ru.practicum.ViewStats(hit.app, hit.uri, count(hit.ip))" +
                    " from EndpointHit as hit" +
                    " where hit.timestamp between :start and :end" +
                    " group by hit.uri, hit.app" +
                    " order by count(hit.uri) desc"
    )
    List<ViewStats> findAllByTime(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end
    );

    @Query(
            "select new ru.practicum.ViewStats(hit.app, hit.uri, count(DISTINCT hit.ip))" +
                    " from EndpointHit as hit" +
                    " where hit.timestamp between :start and :end" +
                    " group by hit.uri, hit.app" +
                    " order by count(hit.uri) desc"
    )
    List<ViewStats> findAllByTimeDistinctIp(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end
    );

    @Query(
            "select new ru.practicum.ViewStats(hit.app, hit.uri, count(hit.ip))" +
                    " from EndpointHit as hit" +
                    " where hit.timestamp between :start and :end" +
                    " and hit.uri in :uris" +
                    " group by hit.uri, hit.app" +
                    " order by count(hit.uri) desc"
    )
    List<ViewStats> findByTimeAndUri(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end,
                                        @Param("uris") String[] uris
    );

    @Query(
            "select new ru.practicum.ViewStats(hit.app, hit.uri, count(DISTINCT hit.ip))" +
                    " from EndpointHit as hit" +
                    " where hit.timestamp between :start and :end" +
                    " and hit.uri in :uris" +
                    " group by hit.uri, hit.app" +
                    " order by count(hit.uri) desc"
    )
    List<ViewStats> findByTimeAndUriDistinctIp(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end,
                                     @Param("uris") String[] uris
    );

    EndpointHit findByIp(String ip);
}
