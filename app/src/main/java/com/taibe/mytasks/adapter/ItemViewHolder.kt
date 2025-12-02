package com.taibe.mytasks.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.taibe.mytasks.databinding.ListItemBinding
import com.taibe.mytasks.entity.Task

class ItemViewHolder(private val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setData(task: Task) {
        binding.tvTitle.text = task.title
        binding.tvDate.text = task.date
    }
}