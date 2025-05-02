package app.ct5.astrocam

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : AppCompatActivity() {
    lateinit var previewView: PreviewView
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    lateinit var imageCapture: ImageCapture;

    val imageResources = arrayOf(
        R.drawable.raw_icon,
        R.drawable.jpg_icon
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.cameraPreview)

        val captureButton: ImageView = findViewById(R.id.captureButton)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
            window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        }

        val picker = findViewById<HorizontalPicker>(R.id.mode_picker_view)
        picker.setValues(arrayOf("Astro", "Photo"))

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf())
        val colors = intArrayOf(Color.BLACK, Color.WHITE)
        tabLayout.tabIconTint = ColorStateList(states, colors)

        imageCapture = ImageCapture.Builder().build()

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

        var currentImageIndex = 0
        val formatButton: ImageView = findViewById(R.id.formatButton)
        formatButton.setOnClickListener {

            currentImageIndex = (currentImageIndex + 1) % imageResources.size
            formatButton.setImageResource(imageResources[currentImageIndex])
            if (currentImageIndex == 0) {
            } else if (currentImageIndex == 1) {
            }
        }

        captureButton.setOnClickListener {
            if (currentImageIndex == 0) {
            } else if (currentImageIndex == 1) {
            }
        }
    }
}
