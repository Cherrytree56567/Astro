package app.ct5.astrocam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var modeRecyclerView: RecyclerView
    private lateinit var modeAdapter: ModeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val modes = listOf("Photo", "Video", "Portrait", "Panorama", "Night")

        modeRecyclerView = findViewById(R.id.modeSelector)
        modeAdapter = ModeAdapter(modes)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        modeRecyclerView.layoutManager = layoutManager
        modeRecyclerView.adapter = modeAdapter

        // Snap to center
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(modeRecyclerView)

        // Detect which item is centered
        modeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager)
                    centerView?.let {
                        val position = layoutManager.getPosition(it)
                        modeAdapter.setSelectedPosition(position)
                    }
                }
            }
        })

        // Start centered
        modeRecyclerView.post {
            modeRecyclerView.scrollToPosition(modes.size / 2)
        }
    }
}