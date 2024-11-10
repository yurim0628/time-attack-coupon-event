package org.example.auth.authentication.constant;

public class SecurityConstants {

    public static final String LOGIN_URL_PATTERN = "/auth/login";
    public static final String DEFAULT_HTTP_METHOD = "POST";

    public static final String EMAIL_ATTRIBUTE = "email";

    public static final String BEARER_TYPE = "Bearer";
    public static final String CLAIM_KEY = "auth";
    public static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;
}
