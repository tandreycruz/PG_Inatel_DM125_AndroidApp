package com.taibe.mytasks.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.taibe.mytasks.adapter.ListAdapter
import com.taibe.mytasks.databinding.ActivityMainBinding
import com.taibe.mytasks.entity.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
    }

    override fun onResume() {
        super.onResume()

        //adapter.addItem(Task(title = "Minha tarefa", date = "25/11/2025"))
    }

    private fun initComponents(){
        adapter = ListAdapter()
        binding.rvMain.adapter = adapter

        //for (i in 1 .. 20) {
        //    adapter.addItem(Task(title = "Minha tarefa $i", date = "25/11/2025"))
        //}

        binding.fabNew.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }
    }
}