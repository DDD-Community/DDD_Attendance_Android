package com.ddd.attendance.data.api.datasource

import com.ddd.attendance.data.api.AttendanceApi
import com.ddd.attendance.data.api.model.attendance.AttendanceChangeRequest
import com.ddd.attendance.data.api.model.attendance.AttendanceRequest
import com.ddd.attendance.data.datasource.ApiAttendanceDataSource
import retrofit2.HttpException
import javax.inject.Inject

class ApiAttendanceDataSourceImpl @Inject constructor(
    private val attendanceApi: AttendanceApi
): ApiAttendanceDataSource {

    override suspend fun attendancesChange(
        attendanceId: Long,
        scheduleId: Long,
        status: String,
        userId: Long
    ): Result<Unit> {
        return try {
            val response =
                attendanceApi.attendancesChange(
                    request = AttendanceChangeRequest(
                        attendanceId = attendanceId,
                        scheduleId = scheduleId,
                        status = status,
                        userId = userId
                    )
                )
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun attendances(qrCode: String): Result<Unit> {
        return try {
            val response =
            attendanceApi.attendances(
                request = AttendanceRequest(
                    qrCode = qrCode
                )
            )
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}