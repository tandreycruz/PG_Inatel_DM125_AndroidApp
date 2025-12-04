package com.taibe.mytasks.listener

import com.taibe.mytasks.entity.Task

interface ClickListener {

    fun onClick(task: Task)
    fun onComplete(id: Long)

}