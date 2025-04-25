package app.ct5.astrocam

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ModeAdapter(private val modes: List<String>) :
    RecyclerView.Adapter<ModeAdapter.ModeViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ModeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modeText: TextView = itemView.findViewById(R.id.modeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mode, parent, false)
        return ModeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModeViewHolder, position: Int) {
        holder.modeText.text = modes[position]

        // Style selected
        if (position == selectedPosition) {
            holder.modeText.setTextColor(Color.WHITE)
            holder.modeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        } else {
            holder.modeText.setTextColor(Color.GRAY)
            holder.modeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }
    }

    override fun getItemCount(): Int = modes.size

    fun setSelectedPosition(position: Int) {
        if (selectedPosition != position) {
            val old = selectedPosition
            selectedPosition = position
            notifyItemChanged(old)
            notifyItemChanged(position)
        }
    }
}