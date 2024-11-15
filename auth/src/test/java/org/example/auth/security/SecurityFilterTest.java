package org.example.auth.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.auth.authentication.login.LoginRequest;
import org.example.auth.user.domain.User;
import org.example.auth.user.service.port.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class SecurityFilterTest {

    private static final String LOGIN_ENDPOINT = "/users/login";
    private static final String VALID_EMAIL = "valid@email.com";
    private static final String INVALID_EMAIL = "invalid@email.com";
    private static final String VALID_PASSWORD = "valid";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("[SUCCESS] 로그인 시, 올바른 이메일과 비밀번호를 입력하면 성공 상태를 반환한다.")
    public void loginSuccess() throws Exception {
        // given
        createUser();
        String requestBodyJson = createLoginRequestBody(VALID_EMAIL, VALID_PASSWORD);

        // when
        ResultActions resultActions = performPostRequest(LOGIN_ENDPOINT, requestBodyJson);

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("[FAIL] 로그인 시, 잘못된 이메일을 입력하면 인증 실패 상태를 반환한다.")
    public void loginFail() throws Exception {
        // given
        createUser();
        String requestBodyJson = createLoginRequestBody(INVALID_EMAIL, VALID_PASSWORD);

        // when
        ResultActions resultActions = performPostRequest(LOGIN_ENDPOINT, requestBodyJson);

        // then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Transactional
    protected void createUser() {
        User user = buildUser();
        userRepository.save(user);
    }

    private User buildUser() {
        String encodedPassword = passwordEncoder.encode(VALID_PASSWORD);
        return User.builder()
                .email(VALID_EMAIL)
                .password(encodedPassword)
                .build();
    }

    private String createLoginRequestBody(String email, String password) throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest(email, password);
        return objectMapper.writeValueAsString(loginRequest);
    }

    private ResultActions performPostRequest(String endpoint, String requestBodyJson) throws Exception {
        return mvc.perform(
                post(endpoint)
                        .content(requestBodyJson)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andDo(print());
    }
}
