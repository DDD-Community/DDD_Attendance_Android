package com.ddd.attendance.data.mapper

import com.ddd.attendance.domain.model.UsersException
import org.json.JSONObject
import retrofit2.HttpException

fun HttpException.toDomainException(tag: String): Throwable {
    val errorBody = response()?.errorBody()?.string()

    val message = try {
        JSONObject(errorBody ?: "").optString("message", "요청 실패")
    } catch (e: Exception) {
        "요청 실패"
    }

    return when (code()) {
        400 -> UsersException.BadRequest(message)
        401 -> UsersException.Unauthorized(message)
        403 -> UsersException.Forbidden(message)
        404 -> UsersException.NotFound(message)
        500 -> UsersException.InternalServerError(message)
        else -> UsersException.Unknown(code(), message)
    }
}