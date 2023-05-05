package com.drsync.core.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drsync.core.data.remote.response.Result
import com.drsync.core.databinding.ItemPokemonBinding
import com.drsync.core.util.Constant.getId
import com.drsync.core.util.Constant.listPokemonImageUrl
import com.drsync.core.util.ConstantFunction.createProgress


class ItemPokemonPagingAdapter(
    private val onItemClick: (Result) -> Unit
) : PagingDataAdapter<Result, ItemPokemonPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Result) {
            binding.apply {
                tvName.text = data.name
                Glide.with(itemView.context)
                    .load(data.getId.listPokemonImageUrl)
                    .placeholder(itemView.context.createProgress())
                    .error(android.R.color.darker_gray)
                    .into(imgItem)

                imgItem.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                    val bitmap = Bitmap.createBitmap(
                        imgItem.width,
                        imgItem.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    imgItem.draw(canvas)
                    cardBg.setCardBackgroundColor(Palette.from(bitmap).generate().getDominantColor(Color.WHITE))
                }

                itemView.setOnClickListener {
                    onItemClick(data)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Result> =
            object : DiffUtil.ItemCallback<Result>() {
                override fun areItemsTheSame(
                    oldItem: Result,
                    newItem: Result
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: Result,
                    newItem: Result
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}