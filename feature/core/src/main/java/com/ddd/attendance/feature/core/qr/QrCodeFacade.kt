package com.ddd.attendance.feature.core.qr

import android.graphics.Bitmap

/**
 * QR 코드 관련 기능을 제공하는 파사드 클래스
 */
object QrCodeFacade {
    
    private val generator = QrCodeGenerator()
    private val scanner = QrCodeScanner()
    
    /**
     * 문자열을 QR 코드 비트맵으로 변환
     * @param text QR 코드로 변환할 문자열
     * @param width QR 코드 이미지 너비 (기본값: 512)
     * @param height QR 코드 이미지 높이 (기본값: 512)
     * @return QR 코드 비트맵 결과
     */
    fun generateQrCode(
        text: String,
        width: Int = 512,
        height: Int = 512
    ): Result<Bitmap> {
        return generator.generateQrCode(text, width, height)
    }
    
    /**
     * 비트맵에서 QR 코드를 스캔하여 문자열로 변환
     * @param bitmap QR 코드가 포함된 비트맵
     * @return 스캔된 문자열 결과
     */
    fun scanQrCode(bitmap: Bitmap): Result<String> {
        return scanner.scanQrCode(bitmap)
    }
    
    /**
     * 카메라 데이터(YUV 바이트 배열)에서 QR 코드를 스캔하여 문자열로 변환
     * @param data YUV 형식의 카메라 데이터
     * @param width 이미지 너비
     * @param height 이미지 높이
     * @return 스캔된 문자열 결과
     */
    fun scanQrCodeFromCamera(
        data: ByteArray,
        width: Int,
        height: Int
    ): Result<String> {
        return scanner.scanQrCodeFromByteArray(data, width, height)
    }
}

/**
 * QR 코드 관련 확장 함수들
 */

/**
 * 문자열을 QR 코드로 변환하는 확장 함수
 */
fun String.toQrCode(width: Int = 512, height: Int = 512): Result<Bitmap> {
    return QrCodeFacade.generateQrCode(this, width, height)
}

/**
 * 비트맵에서 QR 코드를 스캔하는 확장 함수
 */
fun Bitmap.scanQrCode(): Result<String> {
    return QrCodeFacade.scanQrCode(this)
}