package org.example.auth.user.service;

import lombok.RequiredArgsConstructor;
import org.example.auth.user.domain.User;
import org.example.auth.user.domain.dto.RegisterRequest;
import org.example.auth.user.exception.UserException;
import org.example.auth.user.service.port.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.auth.user.exception.UserErrorCode.EMAIL_ALREADY_EXISTS;


@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new UserException(EMAIL_ALREADY_EXISTS);
        }

        User user = User.from(
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.password())
        );
        userRepository.save(user);
    }
}
