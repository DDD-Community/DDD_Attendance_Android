package com.ddd.attendance.feature.core.qr

class QrScanThrottle(
    private val intervalMs: Long = 3_000L
) {
    private var lastEmitTime = 0L

    fun tryEmit(action: () -> Unit) {
        val now = System.currentTimeMillis()
        if (now - lastEmitTime >= intervalMs) {
            lastEmitTime = now
            action()
        }
    }
}