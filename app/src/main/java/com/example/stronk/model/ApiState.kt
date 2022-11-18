package com.example.stronk.model

import com.example.stronk.R
import com.example.stronk.network.ApiErrorCode

enum class ApiStatus {
    LOADING, SUCCESS, FAILURE
}

class ApiState(
    var status: ApiStatus? = null,
    var message: String = "",
    var code: Int = 0,
)

val ApiState.errorMessage: Int
    get() = when (code) {
        ApiErrorCode.INVALID_DATA.code -> R.string.invalid_data
        ApiErrorCode.DATA_CONSTRAINT.code -> R.string.data_constraint
        ApiErrorCode.NOT_FOUND.code -> R.string.not_found
        ApiErrorCode.INVALID_USER_PASS.code -> R.string.invalid_user_pass
        ApiErrorCode.DBERROR.code -> R.string.db_error
        ApiErrorCode.INVALID_REQ.code -> R.string.invalid_req
        ApiErrorCode.UNAUTHORIZED.code -> R.string.unauthorized
        ApiErrorCode.EMAIL_NOT_VERIFIED.code -> R.string.email_not_verified
        ApiErrorCode.FORBIDDEN.code -> R.string.forbidden
        ApiErrorCode.CONNECTION_ERROR.code -> R.string.connection_error
        else -> R.string.unexpected_error
    }
