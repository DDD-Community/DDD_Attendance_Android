package com.ddd.attendance.feature.core.qr

/**
 * QR 스캔 결과 상태
 */
sealed class QrScanState {
    data class Success(val text: String) : QrScanState()
    data class Error(val message: String) : QrScanState()
    object PermissionRequired : QrScanState()
}