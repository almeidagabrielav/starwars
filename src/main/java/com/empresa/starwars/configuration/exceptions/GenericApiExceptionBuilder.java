package com.empresa.starwars.configuration.exceptions;

import org.springframework.http.HttpStatus;

public class GenericApiExceptionBuilder {

    //region Properties
    private String code;
    private String reason;
    private String message;
    private HttpStatus statusCode;
    private Throwable cause;
    //endregion

    //region Constructor
    GenericApiExceptionBuilder() {
    }
    //endregion

    //region Getters and Setters
    public GenericApiExceptionBuilder code(String code) {
        this.code = code;
        return this;
    }

    public GenericApiExceptionBuilder reason(String reason) {
        this.reason = reason;
        return this;
    }

    public GenericApiExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }

    public GenericApiExceptionBuilder statusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public GenericApiExceptionBuilder cause(Throwable cause) {
        this.cause = cause;
        return this;
    }
    //endregion

    //region Builder
    public GenericApiException build() {
        return new GenericApiException(this.code, this.reason, this.message, this.statusCode, this.cause);
    }
    //endregion

    //region Public Methods
    public String toString() {
        return "GenericApiException.GenericApiExceptionBuilder(code=" + this.code + ", reason=" + this.reason + ", message=" + this.message + ", statusCode=" + this.statusCode + ", cause=" + this.cause + ")";
    }
    //endregion
}
