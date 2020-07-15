package com.empresa.starwars.configuration.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class ApiError {

    //region Properties
    @JsonProperty("code")
    protected String code;

    @JsonProperty("message")
    protected String message;
    //endregion

    //region Constructors
    public ApiError(GenericApiException ex) {
        this.code = ex.getCode();
        this.message = ex.getMessage();
    }

    @ConstructorProperties({"code", "reason", "message"})
    public ApiError(String code, String reason, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError() {
    }
    //endregion

    //region Getters and Setters
    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    //endregion

    //region Builder
    public static ApiErrorBuilder builder() {
        return new ApiErrorBuilder();
    }
    //endregion

    //region Public Methods
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ApiError)) {
            return false;
        } else {
            ApiError other = (ApiError)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label47;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label47;
                    }

                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                return true;
            }
        }
    }

    public String toString() {
        return "ApiError(code=" + this.getCode() + ", message=" + this.getMessage() + ")";
    }
    //endregion

    //region Protected Methods
    protected boolean canEqual(Object other) {
        return other instanceof ApiError;
    }
    //endregion

}