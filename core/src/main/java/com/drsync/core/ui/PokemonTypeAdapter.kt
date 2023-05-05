package com.drsync.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drsync.core.data.remote.response.Type
import com.drsync.core.databinding.ItemTypeBinding

class PokemonTypeAdapter : ListAdapter<Type, PokemonTypeAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Type) {
            binding.apply {
                tvType.text = data.type.name
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Type> =
            object : DiffUtil.ItemCallback<Type>() {
                override fun areItemsTheSame(
                    oldItem: Type,
                    newItem: Type
                ): Boolean {
                    return oldItem.type.name == newItem.type.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: Type,
                    newItem: Type
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}