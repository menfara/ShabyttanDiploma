package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import farkhat.myrzabekov.shabyttan.databinding.ItemForYouBinding

interface OnArtworkClickListenerFirestore {
    fun onArtworkClick(artworkId: String)
}


class ForYouAdapter(
    private var imageDataList: List<Pair<String, String>>,
    private val listener: OnArtworkClickListenerFirestore,
) :
    RecyclerView.Adapter<ForYouAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemForYouBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemForYouBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (imageUrl, documentId) = imageDataList[position]
        Glide.with(holder.binding.imageView.context)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    val width = resource.intrinsicWidth
                    val height = resource.intrinsicHeight
                    val aspectRatio = width.toFloat() / height.toFloat()

                    holder.binding.imageView.setAspectRatio(aspectRatio)
                    return false
                }
            })
            .centerCrop()
            .fitCenter()
            .into(holder.binding.imageView)

        holder.binding.root.setOnClickListener {
            listener.onArtworkClick(documentId)
        }
    }

    override fun getItemCount(): Int = imageDataList.size

    fun addAll(newList: List<Pair<String, String>>) {
        val start = imageDataList.size
        imageDataList = imageDataList.toMutableList().apply { addAll(newList) }
        notifyItemRangeInserted(start, newList.size)
    }

    fun submitList(newList: List<Pair<String, String>>) {
        val diffCallback = ForYouDiffCallback(imageDataList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        imageDataList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ForYouDiffCallback(
        private val oldList: List<Pair<String, String>>,
        private val newList: List<Pair<String, String>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].first == newList[newItemPosition].first

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
    }
}
