package com.taibe.mytasks.service

import com.taibe.mytasks.repository.TaskRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitService {

    private val taskRepository: TaskRepository

    companion object {
        val BASE_URL = "http://192.168.0.17:8080"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createClient())
            .addConverterFactory(createConverter())
            .build()

        taskRepository = retrofit.create(TaskRepository::class.java)
    }

    fun getTaskRepository() = taskRepository

    private fun createClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun createConverter(): Converter.Factory {
        return GsonConverterFactory.create()
    }
}