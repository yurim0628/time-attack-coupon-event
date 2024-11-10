package org.example.auth.authentication;

import lombok.RequiredArgsConstructor;
import org.example.auth.user.domain.User;
import org.example.auth.user.service.port.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.example.auth.authentication.exception.AuthErrorCode.INVALID_CREDENTIALS;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_CREDENTIALS.getMessage()));
        return new PrincipalDetails(user);
    }
}
