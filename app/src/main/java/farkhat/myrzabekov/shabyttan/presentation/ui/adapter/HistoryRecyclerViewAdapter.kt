package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import farkhat.myrzabekov.shabyttan.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface OnArtworkClickListener {
    fun onArtworkClick(artworkId: Long)
}

class HistoryRecyclerViewAdapter(
    private val artworks: List<ArtworkEntity>,
    private val listener: OnArtworkClickListener,
    private val languagePreference: String
) :
    RecyclerView.Adapter<HistoryRecyclerViewAdapter.PaintingViewHolder>() {

    inner class PaintingViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(artwork: ArtworkEntity) {
            binding.apply {

                val titleToShow = if (languagePreference == "ru") artwork.titleRu else artwork.title
                val authorToShow = if (languagePreference == "ru") artwork.creatorRu else artwork.creator
                val dateToShow = if (languagePreference == "ru") artwork.creationDateRu else artwork.creationDate

                title.text = titleToShow
                author.text = authorToShow
                date.text = dateToShow

                if (author.text.isBlank()) author.text =
                    itemView.context.getString(R.string.author_unknown)


                Glide.with(imageViewHistory.context)
                    .load(artwork.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageViewHistory)

                binding.root.setOnClickListener {
                    listener.onArtworkClick(artwork.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingViewHolder {
        val binding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaintingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaintingViewHolder, position: Int) {
        holder.bind(artworks[position])
    }

    override fun getItemCount() = artworks.size
}