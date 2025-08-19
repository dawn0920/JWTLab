package org.example.jwtlab.domain.user.repository;

import org.example.jwtlab.domain.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // 회원가입 저장
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    // 이메일 중복 체크 및 조회
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    // ID 조회
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

}
