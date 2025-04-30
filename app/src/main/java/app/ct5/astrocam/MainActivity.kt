package app.ct5.astrocam

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var textureView: TextureView
    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private lateinit var cameraManager: CameraManager
    private lateinit var previewSize: Size;

    private val cameraId: String by lazy {
        cameraManager.cameraIdList.first { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
            cameraDirection == CameraCharacteristics.LENS_FACING_BACK
        }
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            openCamera()
            configureTransform(width, height)
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean = true
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textureView = findViewById(R.id.cameraPreview)
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        textureView.surfaceTextureListener = surfaceTextureListener

        // UI Setup
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        }

        val picker = findViewById<HorizontalPicker>(R.id.mode_picker_view)
        picker.setValues(arrayOf("Astro", "Photo"))

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf()
        )
        val colors = intArrayOf(Color.BLACK, Color.WHITE)
        val colorStateList = ColorStateList(states, colors)

        tabLayout.tabIconTint = colorStateList
    }

    private fun chooseOptimalSize(choices: Array<Size>, width: Int, height: Int): Size {
        val bigEnough = choices.filter { it.width >= width && it.height >= height }
        return if (bigEnough.isNotEmpty()) {
            bigEnough.minByOrNull { it.width * it.height }!!
        } else {
            choices[0]
        }
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
            return
        }

        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val sizes = map?.getOutputSizes(SurfaceTexture::class.java)

        if (sizes != null) {
            previewSize = chooseOptimalSize(sizes, textureView.width, textureView.height)
        }

        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                startCameraPreview()
            }

            override fun onDisconnected(camera: CameraDevice) {
                camera.close()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                camera.close()
            }
        }, null)
    }

    private fun startCameraPreview() {
        val surfaceTexture = textureView.surfaceTexture!!
        surfaceTexture.setDefaultBufferSize(previewSize.width, previewSize.height)
        val surface = Surface(surfaceTexture)

        previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        previewRequestBuilder.addTarget(surface)

        cameraDevice.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                cameraCaptureSession = session
                previewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                cameraCaptureSession.setRepeatingRequest(previewRequestBuilder.build(), null, null)
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {}
        }, null)
    }

    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        val rotation = windowManager.defaultDisplay.rotation
        val matrix = android.graphics.Matrix()
        val viewRect = android.graphics.RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = android.graphics.RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
        matrix.setRectToRect(viewRect, bufferRect, android.graphics.Matrix.ScaleToFit.FILL)
        textureView.setTransform(matrix)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraDevice.close()
    }
}
