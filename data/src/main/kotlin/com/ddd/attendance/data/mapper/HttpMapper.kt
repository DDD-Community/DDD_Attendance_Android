package com.ddd.attendance.data.mapper

import android.util.Log
import com.ddd.attendance.domain.model.UsersException
import retrofit2.HttpException

fun HttpException.toDomainException(tag: String): Throwable {
    Log.e(tag, """
    === HTTP ERROR ===
    Status Code: ${code()}
    Error Body: ${response()?.errorBody()?.string()}
    Message: ${response()?.message()}
    ==================
""".trimIndent())

    return when (code()) {
        400 -> UsersException.BadRequest
        401 -> UsersException.Unauthorized
        403-> UsersException.Forbidden
        404 -> UsersException.NotFound
        500 -> UsersException.InternalServerError
        else -> UsersException.Unknown(code())
    }
}