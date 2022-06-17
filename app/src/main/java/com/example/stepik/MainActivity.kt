package com.example.stepik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stepik.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val timer = Timer()
    var taskState = TaskState.FREE
    var taskProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    when (taskState) {
                        TaskState.FREE -> {
                            taskState = TaskState.PRE_WORKING
                            ++taskProgress
                            runOnUiThread{
                                binding.textView.text = "Working..."
                            }
                        }
                        TaskState.PRE_WORKING -> {
                            if (taskProgress < 50) {
                                ++taskProgress

                                runOnUiThread{
                                    binding.progressBar.progress = taskProgress
                                }

                            } else {
                                taskState = TaskState.CONNECTING
                                runOnUiThread {
                                    binding.textView.text = "Connecting to server..."
                                    binding.progressBar.isIndeterminate = true
                                }
                                this.cancel()
                            }
                        }
                        TaskState.CONNECTING -> {
                            taskState = TaskState.POST_WORKING
                            ++taskProgress
                            runOnUiThread {
                                binding.textView.text = "Working..."
                                binding.progressBar.isIndeterminate = false
                                binding.progressBar.progress = taskProgress
                            }
                        }
                        TaskState.POST_WORKING -> {
                            if (taskProgress < 100) {
                                ++taskProgress
                                runOnUiThread {
                                    binding.progressBar.progress = taskProgress
                                }
                            } else {
                                runOnUiThread {
                                    binding.textView.text = "Completed"
                                }
                                taskState = TaskState.COMPLETED
                            }
                            }
                        TaskState.COMPLETED -> {
                            timer.cancel()
                        }
                      }
                    }
                },1000, 250)
        }
    }
}
enum class TaskState {FREE, PRE_WORKING, CONNECTING, POST_WORKING, COMPLETED}

