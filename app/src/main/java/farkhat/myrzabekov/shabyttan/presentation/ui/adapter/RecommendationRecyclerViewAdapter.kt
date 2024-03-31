package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import farkhat.myrzabekov.shabyttan.databinding.ItemRecommendedBinding
import com.bumptech.glide.Glide
import farkhat.myrzabekov.shabyttan.R
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

class RecommendationRecyclerViewAdapter(
    private val artworks: List<ArtworkEntity>,
    private val listener: OnArtworkClickListener
) :
    RecyclerView.Adapter<RecommendationRecyclerViewAdapter.RecommendationViewHolder>() {

    inner class RecommendationViewHolder(private val binding: ItemRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(artwork: ArtworkEntity) {
            Glide.with(binding.imageViewRecommended.context)
                .load(artwork.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.imageViewRecommended)

            binding.root.setOnClickListener {
                listener.onArtworkClick(artwork.firestoreId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding =
            ItemRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(artworks[position])

        val layoutParams = holder.itemView.layoutParams
        val displayMetrics = Resources.getSystem().displayMetrics
        layoutParams.width = displayMetrics.widthPixels - 16.dp - 16.dp
    }

    override fun getItemCount() = artworks.size
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dp: Float
    get() = this * Resources.getSystem().displayMetrics.density