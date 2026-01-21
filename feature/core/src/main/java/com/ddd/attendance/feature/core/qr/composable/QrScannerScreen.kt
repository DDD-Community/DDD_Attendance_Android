package com.ddd.attendance.feature.core.qr.composable

import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ddd.attendance.feature.core.R
import com.ddd.attendance.feature.core.qr.QrScanState
import com.ddd.attendance.feature.core.qr.QrScanThrottle
import com.ddd.attendance.feature.core.qr.util.QrCameraScanner
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BorderEnabled
import com.ddd.attendance.feature.designsystem.theme.Transparent
import com.ddd.attendance.feature.designsystem.theme.Typography

/**
 * QR 카메라 스캐너 컴포넌트
 * 간단한 콜백 또는 상태 기반 결과 모두 지원
 */
@Composable
fun QrScannerScreen(
    modifier: Modifier = Modifier,
    isAttendanceSuccess: Boolean = false,
    onQrCodeDetected: ((String) -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
    onResult: ((QrScanState) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScanner = remember { QrCameraScanner() }

    val qrThrottle = remember { QrScanThrottle(3_000L) }

    Box(modifier = modifier) {
        // 카메라 뷰
        AndroidView(
            modifier = Modifier.fillMaxSize(),
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
                        qrThrottle.tryEmit {
                            onQrCodeDetected?.invoke(qrText)
                            onResult?.invoke(QrScanState.Success(qrText))
                        }
                    },
                    onError = { exception ->
                        val errorMsg = exception.message ?: "Unknown camera error"
                        onError?.invoke(errorMsg)
                        onResult?.invoke(QrScanState.Error(errorMsg))
                    }
                )
            }
        )
        
        // 딤처리 오버레이와 스캔 영역
        QrScanOverlay(
            modifier = Modifier.fillMaxSize(),
            isAttendanceSuccess = isAttendanceSuccess,
            isActive = qrThrottle.isActive
        )
    }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraScanner.stopCamera()
        }
    }
}

/**
 * QR 스캔 오버레이 (딤처리 + 중앙 스캔 영역)
 */
@Composable
private fun QrScanOverlay(
    modifier: Modifier = Modifier,
    isAttendanceSuccess: Boolean,
    isActive: Boolean,
) {
    val scanAreaSize = 200.dp
    val cornerRadius = 40.dp
    val guideTextBottomMargin = 20.dp
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // 딤처리 Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val scanAreaSizePx = scanAreaSize.toPx()
            val cornerRadiusPx = cornerRadius.toPx()
            
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f
            val scanLeft = centerX - scanAreaSizePx / 2f
            val scanTop = centerY - scanAreaSizePx / 2f
            
            // 전체 화면에 딤 처리 (0x88000000)
            drawRect(
                color = Color(0x88000000),
                size = size
            )
            
            // 중앙 스캔 영역만 투명하게
            drawRoundRect(
                color = Color.Transparent,
                topLeft = androidx.compose.ui.geometry.Offset(scanLeft, scanTop),
                size = androidx.compose.ui.geometry.Size(scanAreaSizePx, scanAreaSizePx),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadiusPx),
                blendMode = BlendMode.Clear
            )
            
            drawRoundRect(
                color = if (isActive) BorderEnabled else Transparent,
                topLeft = androidx.compose.ui.geometry.Offset(scanLeft, scanTop),
                size = androidx.compose.ui.geometry.Size(scanAreaSizePx, scanAreaSizePx),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadiusPx),
                style = if (isActive) Stroke(width = 4.dp.toPx()) else Stroke(width = 0.dp.toPx())
            )
        }

        if (isAttendanceSuccess) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_qr_check),
                    contentDescription = "QR 체크 아이콘"
                )

                DddText(
                    modifier = Modifier.padding(bottom = scanAreaSize + guideTextBottomMargin.times(2)),
                    text = stringResource(R.string.attendance_complete),
                    style = Typography.bodyLargeM,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            DddText(
                modifier = Modifier.padding(bottom = scanAreaSize + guideTextBottomMargin.times(2)),
                text = stringResource(R.string.scan_qr_code),
                style = Typography.bodyLargeM,
                textAlign = TextAlign.Center
            )
        }
    }
}