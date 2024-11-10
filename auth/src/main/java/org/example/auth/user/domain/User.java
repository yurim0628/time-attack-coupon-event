package org.example.auth.user.domain;

import lombok.Builder;
import lombok.Getter;

import static org.example.auth.user.domain.Authority.ROLE_USER;

@Getter
public class User {

    private final Long id;
    private final String email;
    private final String password;
    private final Authority authority;

    @Builder
    private User(Long id, String email, String password, Authority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }

    public static User from(String email, String password) {
        return User.builder()
                .email(email)
                .password(password)
                .authority(ROLE_USER)
                .build();
    }
}
