package com.ddd.attendance.domain.usecase

import com.ddd.attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class UserInfo(
    val name: String,
    val generation: String,
    val attendanceCount: Int,
    val lateCount: Int,
    val absentCount: Int
)

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserInfo> {
        return flow {
            combine(
                userRepository.getUserName(),
                userRepository.getUserGeneration()
            ) { name, generation ->
                UserInfo(
                    name = name,
                    generation = generation,
                    attendanceCount = userRepository.getAttendanceCount(),
                    lateCount = userRepository.getLateCount(),
                    absentCount = userRepository.getAbsentCount()
                )
            }.collect { emit(it) }
        }
    }
}
