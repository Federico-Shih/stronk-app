package com.example.stronk.network

enum class ApiErrorCode(val code: Int) {
    UNEXPECTED(0),
    INVALID_DATA(1),
    DATA_CONSTRAINT(2),
    NOT_FOUND(3),
    INVALID_USER_PASS(4),
    DBERROR(5),
    INVALID_REQ(6),
    UNAUTHORIZED(7),
    EMAIL_NOT_VERIFIED(8),
    FORBIDDEN(9),
    CONNECTION_ERROR(98),
    UNEXPECTED_ERROR(99)
}

/*
    companion object  {
        const val CONNECTION_ERROR_CODE = 98
        const val UNEXPECTED_ERROR_CODE = 99
    }
 */