package com.ddd.attendance.domain.model

sealed class UsersException : RuntimeException() {

    abstract val errorMessage: String

    class BadRequest(
        override val errorMessage: String
    ) : UsersException()

    class Unauthorized(
        override val errorMessage: String
    ) : UsersException()

    class Forbidden(
        override val errorMessage: String
    ) : UsersException()

    class NotFound(
        override val errorMessage: String
    ) : UsersException()

    class InternalServerError(
        override val errorMessage: String
    ) : UsersException()

    class Unknown(
        val code: Int,
        override val errorMessage: String
    ) : UsersException()
}