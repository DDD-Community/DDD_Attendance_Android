package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.MeApi
import com.ddd.attendance.data.datasource.ApiMeDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.model.ScheduleResponse
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiMeDataSourceImpl @Inject constructor(
    private val meApi: MeApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ApiMeDataSource {

    override suspend fun getMe(): Result<Unit> {
        return try {
            Log.d(TAG, "=== API GET ME REQUEST ===")

            val response = meApi.getMe()

            // 사용자 데이터 저장 (accessToken, refreshToken은 유지)
            val currentAccessToken = userPreferencesDataStore.accessToken.firstOrNull() ?: ""
            val currentRefreshToken = userPreferencesDataStore.refreshToken.firstOrNull() ?: ""

            userPreferencesDataStore.saveLoginData(
                userId = response.userId ?: 0,
                name = response.name ?: "",
                email = response.email ?: "",
                generation = response.generation ?: "",
                team = response.team ?: "",
                jobRole = response.jobRole ?: "",
                role = response.role ?: "",
                accessToken = currentAccessToken,
                refreshToken = currentRefreshToken
            )

            Log.d(TAG, "Get Me successful - User ID: ${response.userId}, Name: ${response.name}, Email: ${response.email}")
            Log.d(TAG, "Generation: ${response.generation}, Team: ${response.team}, JobRole: ${response.jobRole}, Role: ${response.role}")
            Log.d(TAG, "User data saved to DataStore")

            // 출석 데이터 가져오기
            try {
                Log.d(TAG, "=== API GET ME ATTENDANCES REQUEST ===")
                val attendanceResponse = meApi.getAttendances()

                userPreferencesDataStore.saveAttendanceData(
                    totalAttended = attendanceResponse.totalAttended,
                    totalLate = attendanceResponse.totalLate,
                    totalAbsent = attendanceResponse.totalAbsent
                )

                Log.d(TAG, "Attendance data saved - Attended: ${attendanceResponse.totalAttended}, Late: ${attendanceResponse.totalLate}, Absent: ${attendanceResponse.totalAbsent}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get attendance data, using default values", e)
            }

            Result.success(Unit)
        } catch (e: HttpException) {
            val errorCode = e.code()
            val errorBody = e.response()?.errorBody()?.string()

            Log.e(TAG, "=== HTTP ERROR ===")
            Log.e(TAG, "Status Code: $errorCode")
            Log.e(TAG, "Error Body: $errorBody")
            Log.e(TAG, "Message: ${e.message}")
            Log.e(TAG, "==================")

            Result.failure(Exception("HTTP $errorCode: $errorBody"))
        } catch (e: Exception) {
            Log.e(TAG, "Get Me failed", e)
            Result.failure(e)
        }
    }

    override suspend fun getSchedules(): Result<List<ScheduleResponse>> {
        return try {
            Log.d(TAG, "=== API GET ME SCHEDULES REQUEST ===")

            val response = meApi.getSchedules()

            Log.d(TAG, "Get Me Schedules successful - Count: ${response.size}")

            Result.success(response)
        } catch (e: HttpException) {
            val errorCode = e.code()
            val errorBody = e.response()?.errorBody()?.string()

            Log.e(TAG, "=== HTTP ERROR ===")
            Log.e(TAG, "Status Code: $errorCode")
            Log.e(TAG, "Error Body: $errorBody")
            Log.e(TAG, "Message: ${e.message}")
            Log.e(TAG, "==================")

            Result.failure(Exception("HTTP $errorCode: $errorBody"))
        } catch (e: Exception) {
            Log.e(TAG, "Get Me Schedules failed", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ApiMeDataSource"
    }
}
