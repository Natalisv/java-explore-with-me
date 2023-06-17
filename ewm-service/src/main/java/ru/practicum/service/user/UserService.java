package ru.practicum.service.user;

import ru.practicum.model.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);

}
