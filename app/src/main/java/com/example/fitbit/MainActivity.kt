package com.example.fitbit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitbit.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddFood: Button
    private lateinit var adapter: DailyEntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        btnAddFood = findViewById(R.id.btnAddFood)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DailyEntryAdapter(emptyList()) { entry ->
            lifecycleScope.launch(Dispatchers.IO) {
                AppDatabase.getInstance(applicationContext).dailyEntryDao().delete(entry)
            }
        }
        recyclerView.adapter = adapter

        btnAddFood.setOnClickListener {
            startActivity(Intent(this, AddEntryActivity::class.java))
        }

        lifecycleScope.launch {
            AppDatabase.getInstance(applicationContext).dailyEntryDao().getAll().collectLatest { entries ->
                adapter.updateData(entries)
            }
        }
    }
}