package com.ddd.attendance.feature.core.qr.composable

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.ddd.attendance.feature.core.qr.util.QrCameraScanner
import com.ddd.attendance.feature.core.qr.QrScanState

/**
 * QR 카메라 스캐너 컴포넌트
 * 간단한 콜백 또는 상태 기반 결과 모두 지원
 */
@Composable
fun QrScannerScreen(
    modifier: Modifier = Modifier,
    onQrCodeDetected: ((String) -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
    onResult: ((QrScanState) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScanner = remember { QrCameraScanner() }
    
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->
            cameraScanner.startCamera(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = previewView,
                onQrCodeDetected = { qrText ->
                    onQrCodeDetected?.invoke(qrText)
                    onResult?.invoke(QrScanState.Success(qrText))
                },
                onError = { exception ->
                    val errorMsg = exception.message ?: "Unknown camera error"
                    onError?.invoke(errorMsg)
                    onResult?.invoke(QrScanState.Error(errorMsg))
                }
            )
        }
    )
    
    DisposableEffect(Unit) {
        onDispose {
            cameraScanner.stopCamera()
        }
    }
}