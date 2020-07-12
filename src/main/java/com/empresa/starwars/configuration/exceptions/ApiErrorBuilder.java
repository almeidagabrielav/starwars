package com.empresa.starwars.configuration.exceptions;

public class ApiErrorBuilder {

    //region Properties
    private String code;
    private String reason;
    private String message;
    //endregion

    //region Constructor
    ApiErrorBuilder() {
    }
    //endregion

    //region Getters and Setters
    public ApiErrorBuilder code(String code) {
        this.code = code;
        return this;
    }

    public ApiErrorBuilder reason(String reason) {
        this.reason = reason;
        return this;
    }

    public ApiErrorBuilder message(String message) {
        this.message = message;
        return this;
    }
    //endregion

    //region Builder
    public ApiError build() {
        return new ApiError(this.code, this.reason, this.message);
    }
    //endregion

    //region Public Methods
    public String toString() {
        return "ApiError.ApiErrorBuilder(code=" + this.code + ", reason=" + this.reason + ", message=" + this.message + ")";
    }
    //endregion
}
