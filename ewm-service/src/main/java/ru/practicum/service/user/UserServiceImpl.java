package ru.practicum.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        log.debug("Add user: {}", savedUser);
        return savedUser;
    }

    @Transactional
    @Override
    public List<User> getAllUsers(List<Long> ids, Integer from, Integer size) {
        List<User> list;
        if (ids == null || ids.isEmpty()) {
            list = userRepository.findAll();
        } else {
            list = userRepository.findByIds(ids.toArray(new Long[0]));
        }
        return list != null ? list.stream().skip(from).limit(size).collect(Collectors.toList()) :
                Collections.emptyList();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (id != null) {
            try {
                userRepository.deleteById(id);
            } catch (EmptyResultDataAccessException e) {
            }
        }
    }

}
