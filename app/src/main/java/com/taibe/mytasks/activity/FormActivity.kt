package com.taibe.mytasks.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.taibe.mytasks.R
import com.taibe.mytasks.databinding.ActivityFormBinding
import com.taibe.mytasks.entity.Task
import com.taibe.mytasks.extension.hasValue
import com.taibe.mytasks.extension.value
import com.taibe.mytasks.service.TaskService
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    private val taskService: TaskService by viewModels()

    private var taskId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskId = intent.getLongExtra("taskId", -1L).takeIf { it > 0 }

        taskId?.let { id ->
            loadTaskDetails(id)
        }

        intent.extras?.getString(Intent.EXTRA_TEXT)?.let { text ->
            binding.etTitle.setText(text)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        initComponents()
    }

    private fun loadTaskDetails(id: Long) {
        taskService.listDetails(id).observe(this) { response ->

            if (response.error) {
                showAlert(R.string.load_error)
                finish()
                return@observe
            }

            val task = response.value
            if (task == null) {
                showAlert(R.string.load_error)
                finish()
                return@observe
            }

            binding.etTitle.setText(task.title)
            binding.etDescription.setText(task.description)
            binding.etDate.setText(task.formatDate())
            binding.etTime.setText(task.formatTime())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initComponents() {
        setupDatePicker()
        setupTimePicker()

        binding.btSave.setOnClickListener {
            binding.layoutTitle.error = null

            if (binding.etTitle.text.isNullOrEmpty()) {
                binding.layoutTitle.error = ContextCompat.getString(this, R.string.title_required)
            } else {
                val date = if (binding.etDate.hasValue()) {
                    LocalDate.parse(binding.etDate.value(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                } else null

                val time = if (binding.etTime.hasValue()) {
                    LocalTime.parse(binding.etTime.value(), DateTimeFormatter.ofPattern("HH:mm"))
                } else null

                val task = Task(
                    id = taskId,
                    title = binding.etTitle.value(),
                    description = binding.etDescription.value(),
                    date = date,
                    time = time
                )

                if (taskId == null) {
                    taskService.create(task).observe(this) { response ->
                        if (response.error) {
                            showAlert(R.string.create_error)
                        } else {
                            finish()
                        }
                    }
                } else {
                    taskService.update(task).observe(this) { response ->
                        if (response.error) {
                            showAlert(R.string.update_error)
                        } else {
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun showAlert(message: Int) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setNeutralButton(android.R.string.ok, null)
            .create()
            .show()
    }

    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val year = calendar.get(java.util.Calendar.YEAR)
            val month = calendar.get(java.util.Calendar.MONTH)
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

            val datePicker = android.app.DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                    val formatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    binding.etDate.setText(formatted)
                },
                year,
                month,
                day
            )

            //datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }
    }

    private fun setupTimePicker() {
        binding.etTime.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            val minute = calendar.get(java.util.Calendar.MINUTE)

            val timePicker = android.app.TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val time = LocalTime.of(selectedHour, selectedMinute)
                    val formatted = time.format(DateTimeFormatter.ofPattern("HH:mm"))
                    binding.etTime.setText(formatted)
                },
                hour,
                minute,
                true // formato 24h
            )

            timePicker.show()
        }
    }
}