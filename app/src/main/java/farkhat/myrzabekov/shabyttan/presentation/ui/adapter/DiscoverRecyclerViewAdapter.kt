package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import farkhat.myrzabekov.shabyttan.databinding.ItemDiscoverImageBinding

class DiscoverRecyclerViewAdapter(private val drawableList: List<Int>) : RecyclerView.Adapter<DiscoverRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDiscoverImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDiscoverImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drawableResId = drawableList[position]
        holder.binding.imageView.setImageResource(drawableResId)
    }

    override fun getItemCount(): Int {
        return drawableList.size
    }
}