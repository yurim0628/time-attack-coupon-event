package org.example.auth.user.service.port;

import org.example.auth.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
