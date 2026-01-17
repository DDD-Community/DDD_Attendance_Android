package com.ddd.attendance.data.model

import retrofit2.Response

data class CodeResult<T>(
    val code: Int,
    val response: Response<T>
)