package com.ddd.attendance.feature.core.qr.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

internal class QrCodeGenerator {
    
    fun generateQrCode(
        text: String,
        width: Int = 512,
        height: Int = 512
    ): Result<Bitmap> {
        return try {
            val writer = MultiFormatWriter()
            val hints = hashMapOf<EncodeHintType, Any>(EncodeHintType.MARGIN to 0)
            val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            
            Result.success(bitmap)
        } catch (e: WriterException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}