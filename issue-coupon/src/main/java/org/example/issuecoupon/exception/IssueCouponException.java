package org.example.issuecoupon.exception;

import lombok.Getter;

@Getter
public class IssueCouponException extends RuntimeException {

    private final ErrorCode errorCode;

    public IssueCouponException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public IssueCouponException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
