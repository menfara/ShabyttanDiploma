package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.databinding.ItemPaintingBinding


class ArtistRecyclerViewAdapter(
    private val artworks: List<ArtworkEntity>,
    private val listener: OnArtworkClickListener
) :
    RecyclerView.Adapter<ArtistRecyclerViewAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemPaintingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artwork: ArtworkEntity) {
            Glide.with(binding.imageView.context)
                .load(artwork.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.imageView)
            binding.root.setOnClickListener {
                listener.onArtworkClick(artwork.firestoreId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPaintingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artworks[position])
    }

    override fun getItemCount() = artworks.size
}