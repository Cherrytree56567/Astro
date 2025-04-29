package app.ct5.astrocam

import app.ct5.astrocam.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val picker = findViewById<HorizontalPicker?>(R.id.mode_picker_view)
        picker.setValues(arrayOf<String>("Astro", "Photo"))

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf()
        )
        val colors = intArrayOf(Color.BLACK, Color.WHITE)
        val csl = ColorStateList(states, colors)

        tabLayout.tabIconTint = csl;
    }
}