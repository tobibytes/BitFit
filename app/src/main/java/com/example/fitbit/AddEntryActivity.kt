package com.example.fitbit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitbit.data.AppDatabase
import com.example.fitbit.data.DailyEntry
import com.google.android.material.slider.Slider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEntryActivity : AppCompatActivity() {
    private lateinit var etFoodName: EditText
    private lateinit var etCalories: EditText
    private lateinit var sliderWaterIntake: Slider
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        etFoodName = findViewById(R.id.etFoodName)
        etCalories = findViewById(R.id.etCalories)
        sliderWaterIntake = findViewById(R.id.sliderWaterIntake)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            val foodName = etFoodName.text.toString()
            val calories = etCalories.text.toString().toIntOrNull()
            val waterIntake = sliderWaterIntake.value.toInt()

            if (foodName.isNotEmpty() && calories != null) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())
                val dailyEntry = DailyEntry(foodName = foodName, calories = calories, waterIntake = waterIntake, date = currentDate)

                lifecycleScope.launch(Dispatchers.IO) {
                    AppDatabase.getInstance(applicationContext).dailyEntryDao().insert(dailyEntry)
                    finish()
                }
            }
        }
    }
}