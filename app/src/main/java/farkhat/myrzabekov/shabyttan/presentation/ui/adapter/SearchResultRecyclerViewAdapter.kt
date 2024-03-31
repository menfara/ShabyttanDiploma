package farkhat.myrzabekov.shabyttan.presentation.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.databinding.ItemSearchResultBinding

class SearchResultRecyclerViewAdapter(
    private val context: Context,
    private val itemList: List<ArtworkEntity>,
    private val languagePreference: String,
    private val onArtworkClickListener: OnArtworkClickListener
) :
    RecyclerView.Adapter<SearchResultRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ItemSearchResultBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(context).load(item.imageUrl).into(holder.binding.imageView)
        if (languagePreference == "ru") {
            holder.binding.apply {
                titleTextView.text = item.titleRu
                artistTextView.text = item.creatorRu
                chipGroup.removeAllViews()
            }

            addChip(holder.binding.chipGroup, item.creationDateRu.toString())
            addChip(holder.binding.chipGroup, item.typeRu)
            addChip(holder.binding.chipGroup, item.techniqueRu)
        } else {
            holder.binding.apply {
                titleTextView.text = item.title
                artistTextView.text = item.creator
                chipGroup.removeAllViews()
            }

            addChip(holder.binding.chipGroup, item.creationDate.toString())
            addChip(holder.binding.chipGroup, item.type)
            addChip(holder.binding.chipGroup, item.technique)
        }

        holder.itemView.setOnClickListener {
            onArtworkClickListener.onArtworkClick(item.firestoreId)
        }
    }



    private fun addChip(chipGroup: ChipGroup, text: String) {
        val chip = Chip(chipGroup.context)
        chip.text = text
        chip.isClickable = false
        chip.isCheckable = false

        chipGroup.addView(chip)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root)
}
