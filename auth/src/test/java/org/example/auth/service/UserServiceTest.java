package org.example.auth.service;

import org.example.auth.common.exception.UserException;
import org.example.auth.user.domain.User;
import org.example.auth.user.domain.dto.RegisterRequest;
import org.example.auth.user.service.UserService;
import org.example.auth.user.service.port.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("[ERROR] 이미 존재하는 이메일일 경우 예외를 던진다")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        RegisterRequest registerRequest = new RegisterRequest(email, password);

        given(userRepository.existsByEmail(email)).willReturn(true);

        // When & Then
        thenThrownBy(() -> userService.signUp(registerRequest)).isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[SUCCESS] 이메일이 존재하지 않을 경우 사용자를 저장한다")
    void shouldSaveUserWhenEmailDoesNotExist() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        RegisterRequest registerRequest = new RegisterRequest(email, password);

        given(userRepository.existsByEmail(email)).willReturn(false);
        given(passwordEncoder.encode(password)).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(User.from(email, password));

        // When
        userService.signUp(registerRequest);

        // Then
        then(userRepository).should().save(any(User.class));
    }
}
