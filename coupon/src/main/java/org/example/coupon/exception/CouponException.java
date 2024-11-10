package org.example.coupon.exception;

import lombok.Getter;

@Getter
public class CouponException extends RuntimeException {

    private final ErrorCode errorCode;

    public CouponException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CouponException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
