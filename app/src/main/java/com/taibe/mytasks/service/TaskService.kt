package com.taibe.mytasks.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.taibe.mytasks.entity.Task
import com.taibe.mytasks.repository.ResponseDto

class TaskService : ViewModel() {

    private val taskRepository = RetrofitService().getTaskRepository()

    fun create(task: Task): LiveData<ResponseDto<Task>> {
        val taskLiveData = MutableLiveData<ResponseDto<Task>>()

        taskRepository.create(task).enqueue(ServiceCallBack<Task>(taskLiveData))

        return taskLiveData
    }

    fun list(): LiveData<ResponseDto<List<Task>>> {
        val tasksLiveData = MutableLiveData<ResponseDto<List<Task>>>()

        taskRepository.list().enqueue(ServiceCallBack<List<Task>>(tasksLiveData))

        return tasksLiveData
    }

    fun delete(id: Long): LiveData<ResponseDto<Void>> {
        val liveData = MutableLiveData<ResponseDto<Void>>()

        taskRepository.delete(id).enqueue(ServiceCallBack(liveData))

        return liveData
    }
}