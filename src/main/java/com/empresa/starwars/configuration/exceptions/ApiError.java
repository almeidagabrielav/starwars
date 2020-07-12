package com.empresa.starwars.configuration.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class ApiError {
    @JsonProperty("code")
    protected String code;

    @JsonProperty("message")
    protected String message;

    public ApiError(GenericApiException ex) {
        this.code = ex.getCode();
        this.message = ex.getMessage();
    }

    public static ApiError.ApiErrorBuilder builder() {
        return new ApiError.ApiErrorBuilder();
    }

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

    protected boolean canEqual(Object other) {
        return other instanceof ApiError;
    }

    public String toString() {
        return "ApiError(code=" + this.getCode() + ", message=" + this.getMessage() + ")";
    }

    @ConstructorProperties({"code", "reason", "message"})
    public ApiError(String code, String reason, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError() {
    }

    public static class ApiErrorBuilder {
        private String code;
        private String reason;
        private String message;

        ApiErrorBuilder() {
        }

        public ApiError.ApiErrorBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ApiError.ApiErrorBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public ApiError.ApiErrorBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ApiError build() {
            return new ApiError(this.code, this.reason, this.message);
        }

        public String toString() {
            return "ApiError.ApiErrorBuilder(code=" + this.code + ", reason=" + this.reason + ", message=" + this.message + ")";
        }
    }
}