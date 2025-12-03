package com.taibe.mytasks.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.taibe.mytasks.R
import com.taibe.mytasks.databinding.ActivityFormBinding
import com.taibe.mytasks.entity.Task
import com.taibe.mytasks.service.TaskService

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    private val taskService: TaskService by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initComponents()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initComponents() {
        binding.btSave.setOnClickListener {
            binding.layoutTitle.error = null

            if (binding.etTitle.text.isNullOrEmpty()) {
                binding.layoutTitle.error = ContextCompat.getString(this, R.string.title_required)
            } else {
                val task = Task(title = binding.etTitle.text.toString())
                taskService.create(task).observe(this) { response ->
                    finish()
                }
            }
        }
    }
}