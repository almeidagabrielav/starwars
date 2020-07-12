package com.empresa.starwars.configuration.exceptions;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class GenericApiException extends RuntimeException {

    //region Properties
    private final String code;
    private final String reason;
    private final String message;
    private final HttpStatus statusCode;
    //endregion

    //region Constructors
    public GenericApiException(String code, String reason, String message, HttpStatus statusCode, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
        this.reason = reason;
        this.statusCode = statusCode == null ? HttpStatus.INTERNAL_SERVER_ERROR : statusCode;
    }

    public GenericApiException(String message) {
        super(message);
        this.message = message;
        this.reason = null;
        this.code = "502";
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GenericApiException(String code, String reason, String message, HttpStatus statusCode){
        this.code = code;
        this.reason = reason;
        this.message = message;
        this.statusCode = statusCode;
    }
    //endregion

    //region Getters
    public String getCode() {
        return this.code;
    }

    public String getReason() {
        return this.reason;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
    //endregion

    //region Builder
    public static GenericApiExceptionBuilder builder() {
        return new GenericApiExceptionBuilder();
    }
    //endregion

    //region Public Methods
    public String toString() {
        return "GenericApiException(code=" + this.getCode() + ", reason=" + this.getReason() + ", statusCode=" + this.getStatusCode() + ")";
    }
    //endregion

}
