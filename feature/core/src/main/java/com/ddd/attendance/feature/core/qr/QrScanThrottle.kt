package com.ddd.attendance.feature.core.qr

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QrScanThrottle(
    private val intervalMs: Long = 3_000L
) {
    var isActive by mutableStateOf(false)
        private set

    private var lastEmitTime = 0L
    private var resetJob: Job? = null

    fun tryEmit(action: () -> Unit) {
        val now = System.currentTimeMillis()

        // 마지막 이벤트 이후 intervalMs 만큼 지났는지 확인
        if (now - lastEmitTime >= intervalMs) {
            lastEmitTime = now // 마지막 이벤트 시간 업데이트
            isActive = true   // 이벤트 활성화
            action()          // 실제 QR 스캔 처리 액션 실행

            // 이전에 예약된 false 전환 Job이 있으면 취소
            resetJob?.cancel()
            // 새로운 Job을 만들어 intervalMs 후에 isActive를 false로 되돌림
            resetJob = CoroutineScope(Dispatchers.Default).launch {
                delay(intervalMs)
                isActive = false
            }
        }
    }
}