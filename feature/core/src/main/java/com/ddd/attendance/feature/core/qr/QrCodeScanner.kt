package com.ddd.attendance.feature.core.qr

import android.graphics.Bitmap
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer

class QrCodeScanner {
    
    fun scanQrCode(bitmap: Bitmap): Result<String> {
        return try {
            val intArray = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            
            val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            
            val reader = MultiFormatReader()
            val hints = mapOf(DecodeHintType.TRY_HARDER to true)
            val result = reader.decode(binaryBitmap, hints)
            
            Result.success(result.text)
        } catch (e: NotFoundException) {
            Result.failure(Exception("QR 코드를 찾을 수 없습니다"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun scanQrCodeFromByteArray(
        data: ByteArray,
        width: Int,
        height: Int
    ): Result<String> {
        return try {
            val source = PlanarYUVLuminanceSource(
                data, width, height,
                0, 0, width, height, false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            
            val reader = MultiFormatReader()
            val hints = mapOf(DecodeHintType.TRY_HARDER to true)
            val result = reader.decode(binaryBitmap, hints)
            
            Result.success(result.text)
        } catch (e: NotFoundException) {
            Result.failure(Exception("QR 코드를 찾을 수 없습니다"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// Custom LuminanceSource for camera data
private class PlanarYUVLuminanceSource(
    private val yuvData: ByteArray,
    private val dataWidth: Int,
    private val dataHeight: Int,
    private val left: Int,
    private val top: Int,
    width: Int,
    height: Int,
    reverseHorizontal: Boolean
) : com.google.zxing.LuminanceSource(width, height) {

    override fun getRow(y: Int, row: ByteArray?): ByteArray {
        if (y < 0 || y >= height) {
            throw IllegalArgumentException("Requested row is outside the image: $y")
        }
        val imageWidth = width
        val actualRow = row ?: ByteArray(imageWidth)
        val offset = (y + top) * dataWidth + left
        System.arraycopy(yuvData, offset, actualRow, 0, imageWidth)
        return actualRow
    }

    override fun getMatrix(): ByteArray {
        val imageWidth = width
        val imageHeight = height
        if (imageWidth == dataWidth && imageHeight == dataHeight) {
            return yuvData
        }
        
        val area = imageWidth * imageHeight
        val matrix = ByteArray(area)
        var inputOffset = top * dataWidth + left
        
        if (imageWidth == dataWidth) {
            System.arraycopy(yuvData, inputOffset, matrix, 0, area)
            return matrix
        }
        
        for (y in 0 until imageHeight) {
            val outputOffset = y * imageWidth
            System.arraycopy(yuvData, inputOffset, matrix, outputOffset, imageWidth)
            inputOffset += dataWidth
        }
        return matrix
    }
}