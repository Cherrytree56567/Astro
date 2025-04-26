package app.ct5.astrocam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ModeAdapter(private val modes: List<String>) :
    RecyclerView.Adapter<ModeAdapter.ModeViewHolder>() {

    inner class ModeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val modeText: TextView = itemView.findViewById(R.id.modeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mode, parent, false)
        return ModeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModeViewHolder, position: Int) {
        holder.modeText.text = modes[position]
    }

    override fun getItemCount(): Int = modes.size
}