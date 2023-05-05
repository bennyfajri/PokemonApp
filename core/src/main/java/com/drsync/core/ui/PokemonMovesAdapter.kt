package com.drsync.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.drsync.core.data.remote.response.Move
import com.drsync.core.databinding.ItemMoveBinding

class PokemonMovesAdapter: ListAdapter<Move, PokemonMovesAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemMoveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Move) {
            binding.apply {
                tvMove.text = data.move.name
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Move> =
            object : DiffUtil.ItemCallback<Move>() {
                override fun areItemsTheSame(
                    oldItem: Move,
                    newItem: Move
                ): Boolean {
                    return oldItem.move.name == newItem.move.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: Move,
                    newItem: Move
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}