package com.ddd.attendance.feature.core.qr

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * QR 코드를 생성하여 표시하는 Composable
 */
@Composable
fun QrCodeImage(
    text: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp
) {
    var qrBitmap by remember(text) { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember(text) { mutableStateOf(true) }
    var errorMessage by remember(text) { mutableStateOf<String?>(null) }
    
    LaunchedEffect(text) {
        isLoading = true
        errorMessage = null
        
        withContext(Dispatchers.IO) {
            QrCodeFacade.generateQrCode(text, size.value.toInt(), size.value.toInt())
                .onSuccess { bitmap ->
                    qrBitmap = bitmap
                    isLoading = false
                }
                .onFailure { throwable ->
                    errorMessage = throwable.message ?: "QR 코드 생성 실패"
                    isLoading = false
                }
        }
    }
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = "...",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            errorMessage != null -> {
                BasicText(
                    text = errorMessage!!,
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                )
            }
            qrBitmap != null -> {
                Image(
                    bitmap = qrBitmap!!.asImageBitmap(),
                    contentDescription = "QR Code for: $text",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * QR 코드 스캔 결과를 나타내는 sealed class
 */
sealed class QrScanResult {
    object Scanning : QrScanResult()
    data class Success(val text: String) : QrScanResult()
    data class Error(val message: String) : QrScanResult()
}

/**
 * QR 코드 스캔을 위한 유틸리티 함수
 */
object QrScanUtils {
    suspend fun scanBitmap(bitmap: Bitmap): QrScanResult {
        return withContext(Dispatchers.IO) {
            QrCodeFacade.scanQrCode(bitmap)
                .fold(
                    onSuccess = { text -> QrScanResult.Success(text) },
                    onFailure = { throwable -> 
                        QrScanResult.Error(throwable.message ?: "QR 코드 스캔 실패")
                    }
                )
        }
    }
}