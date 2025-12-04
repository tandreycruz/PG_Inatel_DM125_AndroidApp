package com.taibe.mytasks.adapter

import androidx.recyclerview.widget.RecyclerView
import com.taibe.mytasks.R
import com.taibe.mytasks.databinding.ListItemBinding
import com.taibe.mytasks.entity.Task
import com.taibe.mytasks.listener.ClickListener

class ItemViewHolder(
    private val binding: ListItemBinding,
    private val listener: ClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun setData(task: Task) {
        binding.tvTitle.text = task.title
        binding.tvDate.text = task.formatDateTime()

        if (task.completed) {
            binding.tvTitle.setBackgroundResource(R.color.green)
        } else {
            binding.tvTitle.setBackgroundResource(R.color.blue)
        }

        binding.root.setOnClickListener {
            listener.onClick(task)
        }

        binding.root.setOnCreateContextMenuListener { menu, _, _ ->
            menu.add(R.string.mark_completed).setOnMenuItemClickListener {
                task.id?.let { id -> listener.onComplete(task.id) }
                true
            }
        }
    }
}