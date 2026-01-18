package com.ddd.attendance.domain.model

sealed class UsersException : RuntimeException() {
    object BadRequest : UsersException()
    object Unauthorized : UsersException()
    object Forbidden: UsersException()
    object NotFound : UsersException()
    object InternalServerError : UsersException()
    data class Unknown(val code: Int) : UsersException()
}