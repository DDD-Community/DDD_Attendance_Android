package com.ddd.attendance.feature.core.qr.composable

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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
import com.ddd.attendance.feature.core.qr.util.QrCodeGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * QR 코드를 생성하여 표시하는 Composable
 */
@Composable
fun QrCodeImage(
    text: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    qrCodeBase64: String? = null
) {
    var qrBitmap by remember(text, qrCodeBase64) { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember(text, qrCodeBase64) { mutableStateOf(true) }
    var errorMessage by remember(text, qrCodeBase64) { mutableStateOf<String?>(null) }

    val generator = QrCodeGenerator()

    LaunchedEffect(text, qrCodeBase64) {
        isLoading = true
        errorMessage = null

        try {
            withContext(Dispatchers.IO) {
                // Base64 QR 이미지가 있으면 그대로 사용
                if (!qrCodeBase64.isNullOrBlank()) {
                    qrBitmap = base64ToBitmap(qrCodeBase64)
                } else {
                    // 기존 QR 생성 로직 그대로
                    generator.generateQrCode(
                        text,
                        size.value.toInt(),
                        size.value.toInt()
                    ).getOrThrow()
                        .also { qrBitmap = it }
                }
            }
        } catch (e: Exception) {
            errorMessage = e.message ?: "QR 처리 실패"
        } finally {
            isLoading = false
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
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

fun base64ToBitmap(base64: String): Bitmap {
    val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}
