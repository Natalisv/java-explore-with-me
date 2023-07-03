package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Query("select u from User u where u.id in :ids")
    List<User> findByIds(@Param("ids") Long[] ids);


}
