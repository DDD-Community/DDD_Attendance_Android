package com.ddd.attendance.data.api.datasource

import android.util.Log
import com.ddd.attendance.data.api.SchedulesApi
import com.ddd.attendance.data.datasource.ApiSchedulesDataSource
import com.ddd.attendance.data.datastore.UserPreferencesDataStore
import com.ddd.attendance.data.mapper.toDomainException
import com.ddd.attendance.data.model.ActivityScheduleResponse
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiSchedulesDataSourceImpl @Inject constructor(
    private val schedulesApi: SchedulesApi,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ApiSchedulesDataSource {

    override suspend fun getSchedules(): Result<List<ActivityScheduleResponse>> {
        return try {
            Log.d(TAG, "=== API GET SCHEDULES REQUEST ===")

            val response = schedulesApi.getSchedules()

            Log.d(TAG, "Get Schedules successful - Count: ${response.size}")

            if (response.isNotEmpty()) {
                val sorted = response.sortedWith(compareBy({ it.year }, { it.month }, { it.day }))
                val first = sorted.first()
                val last = sorted.last()

                val startDate = "${first.year}.${String.format("%02d", first.month)}.${String.format("%02d", first.day)}"
                val endDate = "${last.year}.${String.format("%02d", last.month)}.${String.format("%02d", last.day)}"

                userPreferencesDataStore.saveActivityPeriod(startDate, endDate)
                Log.d(TAG, "Activity period saved: $startDate - $endDate")
            }

            Result.success(response)
        } catch (e: HttpException) {
            Result.failure(e.toDomainException(TAG))
        } catch (e: Exception) {
            Log.e(TAG, "Get Schedules failed", e)
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "ApiSchedulesDataSource"
    }
}
