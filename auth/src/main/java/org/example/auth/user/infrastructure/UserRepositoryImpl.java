package org.example.auth.user.infrastructure;

import lombok.RequiredArgsConstructor;
import org.example.auth.user.domain.User;
import org.example.auth.user.infrastructure.entity.UserEntity;
import org.example.auth.user.service.port.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.fromModel(user)).toModel();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
