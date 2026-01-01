package com.ddd.attendance.feature.core.qr.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ddd.attendance.feature.core.qr.QrScanState
import com.ddd.attendance.feature.core.qr.composable.QrCodeImage
import com.ddd.attendance.feature.core.qr.composable.QrScannerScreen

@Composable
fun QrTestScreen(
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("Hello DDD Attendance!") }
    var statusMessage by remember { mutableStateOf("") }
    var showCameraScanner by remember { mutableStateOf(false) }
    var cameraScannedText by remember { mutableStateOf("") }
    
    if (showCameraScanner) {
        QrScannerScreen(
            modifier = Modifier.fillMaxSize(),
            onResult = { result ->
                when (result) {
                    is QrScanState.Success -> {
                        cameraScannedText = result.text
                        showCameraScanner = false
                        statusMessage = "카메라 스캔 완료"
                    }
                    is QrScanState.Error -> {
                        statusMessage = "카메라 스캔 오류: ${result.message}"
                        showCameraScanner = false
                    }
                    is QrScanState.PermissionRequired -> {
                        statusMessage = "카메라 권한이 필요합니다"
                        showCameraScanner = false
                    }
                    else -> {
                        // Idle, Scanning 상태는 그대로 유지
                    }
                }
            }
        )
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        BasicText(
            text = "QR 코드 기능 테스트",
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Black
            )
        )
        
        // Input field
        Column {
            BasicText(
                text = "QR 코드로 변환할 텍스트:",
                style = TextStyle(fontSize = 14.sp)
            )
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                textStyle = TextStyle(fontSize = 16.sp)
            )
        }
        
        // QR Code Display (Compose component)
        if (inputText.isNotBlank()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BasicText(
                    text = "실시간 QR 코드 (Compose 컴포넌트):",
                    style = TextStyle(fontSize = 14.sp)
                )
                QrCodeImage(
                    text = inputText,
                    size = 200.dp
                )
            }
        }
        
        
        // Camera Scan Button
        Box(
            modifier = Modifier
                .background(Color.Magenta, RoundedCornerShape(8.dp))
                .clickable {
                    showCameraScanner = true
                    statusMessage = "카메라 스캔 시작"
                }
                .padding(12.dp)
        ) {
            BasicText(
                text = "카메라로 QR 스캔",
                style = TextStyle(color = Color.White, fontSize = 16.sp)
            )
        }
        
        // Status message
        if (statusMessage.isNotEmpty()) {
            BasicText(
                text = "상태: $statusMessage",
                style = TextStyle(fontSize = 14.sp, color = Color.DarkGray)
            )
        }
        
        
        // Camera Scan result
        if (cameraScannedText.isNotEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BasicText(
                    text = "카메라 스캔 결과:",
                    style = TextStyle(fontSize = 14.sp)
                )
                BasicText(
                    text = cameraScannedText,
                    style = TextStyle(
                        fontSize = 16.sp, 
                        color = Color.Green
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
            // Usage info
            BasicText(
                text = "테스트된 기능:\n" +
                        "1. QrCodeImage() - Compose 컴포넌트 (직접 생성)\n" +
                        "2. QrScannerScreen() - 카메라 스캔",
                style = TextStyle(fontSize = 12.sp, color = Color.Gray)
            )
        }
    }
}