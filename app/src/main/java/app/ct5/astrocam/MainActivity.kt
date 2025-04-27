package app.ct5.astrocam

import app.ct5.astrocam.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val picker = findViewById<HorizontalPicker?>(R.id.mode_picker_view)
        picker.setValues(arrayOf<String>("Astro", "Photo", "Video", "Portrait"))
    }
}