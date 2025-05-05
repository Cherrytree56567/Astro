package app.ct5.astrocam

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.ExifInterface
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.ByteArrayOutputStream
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

fun MainActivity.bindCameraUseCases(
    cameraProvider: ProcessCameraProvider,
    imageCapture: ImageCapture
) {
    val preview = Preview.Builder().build()
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    preview.setSurfaceProvider(previewView.surfaceProvider)
    previewView.scaleType = PreviewView.ScaleType.FILL_START

    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
}


fun MainActivity.CaptureRAW() {
    val newCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setOutputFormat(ImageCapture.OUTPUT_FORMAT_RAW)
        .build()

    imageCapture = newCapture

    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // Rebind with updated imageCapture
        bindCameraUseCases(cameraProvider, imageCapture)

        // Take picture and save to MediaStore
        imageCapture.takePicture(ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(error: ImageCaptureException) {
                    Log.e("MainActivity", "In-memory capture failed: ${error.message}", error)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.e("MainActivity", "Captured Image")
                }
            })
    }, ContextCompat.getMainExecutor(this))
}

fun MainActivity.saveRAW(fileName: String = "photo.dng") {
    val newCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setOutputFormat(ImageCapture.OUTPUT_FORMAT_RAW)
        .build()

    imageCapture = newCapture

    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // Rebind with updated imageCapture
        bindCameraUseCases(cameraProvider, imageCapture)

        // Prepare MediaStore entry
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName.removeSuffix(".dng"))
            put(MediaStore.MediaColumns.MIME_TYPE, "image/x-adobe-dng")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/Camera")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // Take picture and save to MediaStore
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("MainActivity", "Raw save failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.i("MainActivity", "Raw saved to MediaStore (DCIM/Camera)")
                }
            }
        )
    }, ContextCompat.getMainExecutor(this))
}

fun MainActivity.saveRAWJPG(fileName: String = "photo.dng") {
    val newCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setOutputFormat(ImageCapture.OUTPUT_FORMAT_RAW_JPEG)
        .build()

    imageCapture = newCapture

    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // Rebind with updated imageCapture
        bindCameraUseCases(cameraProvider, imageCapture)

        // Prepare MediaStore entry
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName.removeSuffix(".dng"))
            put(MediaStore.MediaColumns.MIME_TYPE, "image/x-adobe-dng")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/Camera")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // Take picture and save to MediaStore
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("MainActivity", "Raw save failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.i("MainActivity", "Raw saved to MediaStore (DCIM/Camera)")
                }
            }
        )
    }, ContextCompat.getMainExecutor(this))
}

fun MainActivity.saveJPG(fileName: String = "photo.jpg") {
    val newCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
        .setOutputFormat(ImageCapture.OUTPUT_FORMAT_JPEG)
        .build()

    imageCapture = newCapture

    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // Rebind with updated imageCapture
        bindCameraUseCases(cameraProvider, imageCapture)

        // Prepare MediaStore entry
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName.removeSuffix(".jpg"))
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/Camera")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // Take picture and save to MediaStore
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("MainActivity", "Raw save failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Log.i("MainActivity", "Raw saved to MediaStore (DCIM/Camera)")
                }
            }
        )
    }, ContextCompat.getMainExecutor(this))
}

enum class ImageFormatOption { PNG, JPEG, DNG }

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalGetImage::class)
fun MainActivity.saveImage(
    imageProxy: ImageProxy,
    fileName: String,
    aperture: Float? = null,
    megapixels: Double? = null,
    exposureTime: String? = null,
    shutterSpeedValue: String? = null,
    whiteBalance: String? = null,
    iso: Int? = null,
    exposureBias: String? = null,
    aspectRatio: String? = null
): File {
    val ext = fileName.substringAfterLast('.').lowercase()
    if (ext != "png" && ext != "jpg" && ext != "jpeg") {
        throw IllegalArgumentException("Filename must end in .png or .jpg/.jpeg")
    }
    val compressFormat = if (ext == "png")
        Bitmap.CompressFormat.PNG
    else
        Bitmap.CompressFormat.JPEG

    val folder = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ?: throw IllegalStateException("No external files dir")
    val outFile = File(folder, fileName)

    val bitmap = imageProxy.toBitmap()
    FileOutputStream(outFile).use { fos ->
        bitmap.compress(compressFormat, 100, fos)
    }
    imageProxy.close()

    try {
        val exif = ExifInterface(outFile.absolutePath)

        aperture?.let      { exif.setAttribute(ExifInterface.TAG_F_NUMBER, it.toString()) }
        exposureTime?.let  { exif.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, it) }
        shutterSpeedValue?.let { exif.setAttribute(ExifInterface.TAG_SHUTTER_SPEED_VALUE, it) }
        iso?.let           { exif.setAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS, it.toString()) }
        whiteBalance?.let  { exif.setAttribute(ExifInterface.TAG_WHITE_BALANCE, it) }
        exposureBias?.let  { exif.setAttribute(ExifInterface.TAG_EXPOSURE_BIAS_VALUE, it) }

        val w = bitmap.width
        val h = bitmap.height
        exif.setAttribute(ExifInterface.TAG_PIXEL_X_DIMENSION, w.toString())
        exif.setAttribute(ExifInterface.TAG_PIXEL_Y_DIMENSION, h.toString())
        megapixels?.let {
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT, String.format("%.1f MP", it))
        }

        if (aspectRatio != null) {
            val comment = exif.getAttribute(ExifInterface.TAG_USER_COMMENT)
            val combined = if (comment.isNullOrEmpty()) aspectRatio else "$comment; Aspect $aspectRatio"
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT, combined)
        }

        exif.setAttribute(ExifInterface.TAG_MAKE,  android.os.Build.MANUFACTURER)
        exif.setAttribute(ExifInterface.TAG_MODEL, android.os.Build.MODEL)

        exif.saveAttributes()
        Log.i("MainActivity", "EXIF embedded into ${outFile.name}")
    } catch (e: Exception) {
        Log.e("MainActivity", "Failed writing EXIF: ${e.message}", e)
    }

    Log.i("MainActivity", "Saved image to ${outFile.absolutePath}")
    return outFile
}