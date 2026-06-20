package com.ddd.attendance.data.api.interceptor

import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Login API는 Authorization 헤더가 필요 없음
        if (originalRequest.url.encodedPath.contains("/api/auth/login")) {
            return chain.proceed(originalRequest)
        }

        // TODO: 운영진/투표 기능 테스트용 하드코딩 토큰. 테스트 후 제거할 것.
        val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyNzgiLCJyb2xlIjoiTUFOQUdFUiIsImV4cCI6NDEwMjQ0NDgwMH0.k7iylOR-v0Sy0B004wn_iD-q4-r1-ccFcx4udPzccTk"

        // AccessToken 가져오기
        // val accessToken = runBlocking {
        //     userPreferencesDataStore.accessToken.firstOrNull()
        // }

        // Authorization 헤더 추가
        val newRequest = if (!accessToken.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}
