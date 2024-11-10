package org.example.auth.user.service.port;

import org.example.auth.user.domain.User;

public interface UserRepository {

    User save(User user);

    boolean existsByEmail(String email);
}
