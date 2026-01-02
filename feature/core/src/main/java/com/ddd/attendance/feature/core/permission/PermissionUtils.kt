package com.ddd.attendance.feature.core.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * 카메라 권한 상태
 */
sealed class CameraPermissionState {
    object Granted : CameraPermissionState()
    object Denied : CameraPermissionState()
}

/**
 * 권한 관리 유틸리티 클래스
 */
object PermissionUtils {
    
    const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    
    /**
     * 카메라 권한 상태 확인
     */
    fun getCameraPermissionState(context: Context): CameraPermissionState {
        return when {
            ContextCompat.checkSelfPermission(
                context,
                CAMERA_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> CameraPermissionState.Granted
            else -> CameraPermissionState.Denied
        }
    }

}

