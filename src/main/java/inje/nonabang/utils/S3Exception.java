package inje.nonabang.utils;

import inje.nonabang.enumSet.ErrorCode;
import lombok.Getter;

@Getter
public class S3Exception extends RuntimeException {
    private final ErrorCode errorCode;

    public S3Exception(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}