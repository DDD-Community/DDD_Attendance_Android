package com.ddd.attendance.feature.core.qr.composable

import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.ddd.attendance.feature.core.qr.QrScanState
import com.ddd.attendance.feature.core.qr.util.QrCameraScanner

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
        
        // 딤처리 오버레이와 스캔 영역
        QrScanOverlay(
            modifier = Modifier.fillMaxSize()
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
    modifier: Modifier = Modifier
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
            
            // 중앙 스캔 영역만 투명하게 (딤 제거)
            drawRoundRect(
                color = Color.Transparent,
                topLeft = androidx.compose.ui.geometry.Offset(scanLeft, scanTop),
                size = androidx.compose.ui.geometry.Size(scanAreaSizePx, scanAreaSizePx),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadiusPx),
                blendMode = androidx.compose.ui.graphics.BlendMode.Clear
            )
        }
        
        // 안내 텍스트 (스캔 영역 위쪽에 20dp 간격으로 배치)
        BasicText(
            text = "QR 코드를 스캔해 주세요",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = scanAreaSize + guideTextBottomMargin.times(2))
        )
    }
}