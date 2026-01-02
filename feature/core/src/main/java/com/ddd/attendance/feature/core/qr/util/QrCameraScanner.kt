package com.ddd.attendance.feature.core.qr.util

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class QrCameraScanner {
    
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onQrCodeDetected: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, QrCodeAnalyzer { qrCode ->
                            onQrCodeDetected(qrCode)
                        })
                    }
                
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
                
            } catch (exc: Exception) {
                onError(exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }
    
    fun stopCamera() {
        cameraExecutor.shutdown()
    }
    
    private class QrCodeAnalyzer(
        private val onQrCodeDetected: (String) -> Unit
    ) : ImageAnalysis.Analyzer {
        
        private val reader = MultiFormatReader().apply {
            val hints = mapOf(DecodeHintType.TRY_HARDER to true)
            setHints(hints)
        }
        
        override fun analyze(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            
            val source = CameraPlanarYUVLuminanceSource(
                data,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height
            )
            
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            
            try {
                val result = reader.decode(binaryBitmap)
                onQrCodeDetected(result.text)
            } catch (e: NotFoundException) {
                // QR 코드가 발견되지 않음 - 정상적인 상황
            } catch (e: Exception) {
                // 다른 에러는 무시
            } finally {
                image.close()
            }
        }
    }
}

// Custom LuminanceSource for camera data (same as in QrCodeScanner but optimized for camera)
private class CameraPlanarYUVLuminanceSource(
    private val yuvData: ByteArray,
    private val dataWidth: Int,
    private val dataHeight: Int,
    private val left: Int,
    private val top: Int,
    width: Int,
    height: Int
) : LuminanceSource(width, height) {

    override fun getRow(y: Int, row: ByteArray?): ByteArray {
        if (y !in 0..<height) {
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