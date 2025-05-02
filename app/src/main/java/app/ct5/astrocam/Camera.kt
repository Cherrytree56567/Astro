package app.ct5.astrocam

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.io.FileOutputStream

fun MainActivity.bindPreview(cameraProvider : ProcessCameraProvider) {
    var preview : Preview = Preview.Builder()
        .build()

    var cameraSelector : CameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    preview.setSurfaceProvider(previewView.getSurfaceProvider())

    previewView.scaleType = PreviewView.ScaleType.FILL_START

    var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageCapture, preview)
}

fun MainActivity.CaptureRAW() {
    val cameraExecutor = null
    val imageMode = ImageCapture.Builder()
        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setOutputFormat(ImageCapture.OUTPUT_FORMAT_RAW)
        .build()
    val image = imageMode.takePicture(ContextCompat.getMainExecutor(this),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onError(error: ImageCaptureException) {
                Log.e("MainActivity", "In-memory capture failed: ${error.message}", error)
            }

            override fun onCaptureSuccess(image: ImageProxy) {
                Log.e("MainActivity", "Captured Image")
            }
        }
    )

    return image
}

fun MainActivity.CaptureJPG() {
    val cameraExecutor = null
    val imageMode = ImageCapture.Builder()
        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setOutputFormat(ImageCapture.OUTPUT_FORMAT_JPEG)
        .build()
    val image = imageMode.takePicture(ContextCompat.getMainExecutor(this),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onError(error: ImageCaptureException) {
                Log.e("MainActivity", "In-memory capture failed: ${error.message}", error)
            }

            override fun onCaptureSuccess(image: ImageProxy) {
                Log.e("MainActivity", "Captured Image")
            }
        }
    )

    return image
}

fun MainActivity.saveBitmapToFile(
    context: Context,
    bitmap: Bitmap,
    fileName: String,
    asPng: Boolean = true
): File {
    val format = if (asPng) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG
    // Use external-files so user doesn’t need WRITE_EXTERNAL_STORAGE on Android Q+
    val folder = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(folder, fileName)
    FileOutputStream(file).use { out ->
        // quality ignored for PNG
        bitmap.compress(format, /* quality= */ 100, out)
    }
    Log.i("MainActivity", "Bitmap saved to ${file.absolutePath}")
    return file
}