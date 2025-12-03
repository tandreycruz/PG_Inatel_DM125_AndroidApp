package com.taibe.mytasks.adapter

import android.content.Context
import android.graphics.pdf.models.ListItem
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.taibe.mytasks.R
import com.taibe.mytasks.databinding.ListItemBinding
import com.taibe.mytasks.entity.Task

class ListAdapter(private val context: Context, private val emptyMessage: TextView) : RecyclerView.Adapter<ItemViewHolder>() {

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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setData(items[position])
    }

    override fun getItemCount() = items.size

    fun getItem(position: Int) = items[position]

    fun setData(data: List<Task>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()

        checkEmptyList()
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)

        checkEmptyList()
    }

    fun checkEmptyList() {
        if (items.isEmpty()) {
            emptyMessage.visibility = View.VISIBLE
            emptyMessage.text = ContextCompat.getString(context, R.string.empty_list)
        } else {
            emptyMessage.visibility = View.INVISIBLE
        }
    }
}