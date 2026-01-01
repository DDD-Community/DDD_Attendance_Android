package com.ddd.attendance.feature.core.qr

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * QR 코드 기능 사용 예제
 * 
 * 사용법:
 * 
 * 1. 기본 사용법 (파사드 방식)
 * ```kotlin
 * // QR 코드 생성
 * val result = QrCodeFacade.generateQrCode("Hello World")
 * result.onSuccess { bitmap ->
 *     // bitmap 사용
 * }
 * 
 * // QR 코드 스캔
 * val scanResult = QrCodeFacade.scanQrCode(bitmap)
 * scanResult.onSuccess { text ->
 *     // 스캔된 텍스트 사용
 * }
 * ```
 * 
 * 2. 확장 함수 사용법
 * ```kotlin
 * // QR 코드 생성
 * val qrResult = "Hello World".toQrCode()
 * 
 * // QR 코드 스캔
 * val scanResult = bitmap.scanQrCode()
 * ```
 * 
 * 3. Compose에서 사용법
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     QrCodeImage(
 *         text = "Hello World",
 *         size = 200.dp
 *     )
 * }
 * ```
 */

@Composable
fun QrCodeDemo(
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("Hello DDD!") }
    var scannedText by remember { mutableStateOf("") }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasicText(
            text = "QR 코드 생성 및 스캔 데모",
            style = TextStyle(fontSize = 18.sp)
        )
        
        BasicTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp),
            textStyle = TextStyle(fontSize = 16.sp)
        )
        
        Box(
            modifier = Modifier
                .background(Color.Blue, RoundedCornerShape(4.dp))
                .clickable {
                    scope.launch {
                        // 파사드 방식으로 QR 코드 생성
                        QrCodeFacade.generateQrCode(inputText)
                            .onSuccess { bitmap ->
                                qrBitmap = bitmap
                            }
                    }
                }
                .padding(12.dp)
        ) {
            BasicText(
                text = "QR 코드 생성",
                style = TextStyle(color = Color.White, fontSize = 16.sp)
            )
        }
        
        // Compose 컴포넌트로 QR 코드 표시
        if (inputText.isNotBlank()) {
            QrCodeImage(
                text = inputText,
                size = 200.dp
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(
            modifier = Modifier
                .background(
                    if (qrBitmap != null) Color.Green else Color.Gray,
                    RoundedCornerShape(4.dp)
                )
                .clickable(enabled = qrBitmap != null) {
                    qrBitmap?.let { bitmap ->
                        scope.launch {
                            // 확장 함수로 QR 코드 스캔
                            bitmap.scanQrCode()
                                .onSuccess { text ->
                                    scannedText = text
                                }
                                .onFailure { 
                                    scannedText = "스캔 실패"
                                }
                        }
                    }
                }
                .padding(12.dp)
        ) {
            BasicText(
                text = "QR 코드 스캔",
                style = TextStyle(color = Color.White, fontSize = 16.sp)
            )
        }
        
        if (scannedText.isNotEmpty()) {
            BasicText(
                text = "스캔 결과: $scannedText",
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}