package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.model.Schedule
import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<Schedule>> = flow {
        val schedules = userRepository.getSchedules()
        emit(schedules.getOrThrow())
    }
}