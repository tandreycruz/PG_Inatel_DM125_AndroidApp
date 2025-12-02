package com.taibe.mytasks.adapter

import android.graphics.pdf.models.ListItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.taibe.mytasks.databinding.ListItemBinding
import com.taibe.mytasks.entity.Task

class ListAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    private val items = mutableListOf<Task>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        holder.setData(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(data: List<Task>) {
        items.clear()
        items.addAll(data)
    }

    fun  addItem(item: Task) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

}