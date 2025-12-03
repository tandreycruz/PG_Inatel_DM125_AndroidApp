package com.taibe.mytasks.repository

import com.taibe.mytasks.entity.Task
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskRepository {

    @POST("/tasks")
    fun create(@Body task: Task) : Call<Task>

    @GET("/tasks")
    fun list(): Call<List<Task>>

    @DELETE("/tasks/{id}")
    fun delete(@Path("id") id: Long): Call<Void>
}