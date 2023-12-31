package com.example.appinsulina.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinsulina.R

class CalculateInsulinActivity: AppCompatActivity() {
  private lateinit var carbohydrate: EditText
  private lateinit var weight: EditText
  private lateinit var glucoseBefore: EditText
  private lateinit var glucoseAfter: EditText
  private lateinit var btnCalculate: Button
  private lateinit var correctionResult: TextView
  private lateinit var mealResult: TextView
  private lateinit var totalResult: TextView
  private lateinit var btnBackArrow: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_calculate_insulin)
    setupView()
    setupListeners()
    setupCachedResult()
  }

  fun setupView() {
    carbohydrate = findViewById(R.id.input_grams)
    weight = findViewById(R.id.input_weight)
    glucoseBefore = findViewById(R.id.input_before_glucose)
    glucoseAfter = findViewById(R.id.input_after_glucose)
    btnCalculate = findViewById(R.id.btn_calculate_insulin)
    correctionResult = findViewById(R.id.txt_correction_dose_result)
    mealResult = findViewById(R.id.txt_meal_dose_result)
    totalResult = findViewById(R.id.txt_total_dose_result)
    btnBackArrow = findViewById(R.id.back_arrow)
  }

  fun setupListeners() {
    btnCalculate.setOnClickListener {
      calculateInsulin()
    }

    btnBackArrow.setOnClickListener {
      finish()
    }
  }

  fun calculateInsulin() {
    val inputCarbohydrate = carbohydrate.text.toString().toFloatOrNull() ?: 0.0f
    val inputWeight = weight.text.toString().toFloatOrNull() ?: 0.0f
    val inputPreGlucose = glucoseBefore.text.toString().toFloatOrNull() ?: 0.0f
    val inputPostGlucose = glucoseAfter.text.toString().toFloatOrNull() ?: 0.0f
    var carbohydrateProportion: Float = 1.0f

    when {
      inputWeight in 45.00..49.00 -> carbohydrateProportion = 16.00f
      inputWeight in 49.50..58.00 -> carbohydrateProportion = 15.00f
      inputWeight in 58.50..62.50 -> carbohydrateProportion = 14.00f
      inputWeight in 63.00..67.00 -> carbohydrateProportion = 13.00f
      inputWeight in 67.50..76.00 -> carbohydrateProportion = 12.00f
      inputWeight in 76.50..80.50 -> carbohydrateProportion = 11.00f
      inputWeight in 81.00..85.50 -> carbohydrateProportion = 10.00f
      inputWeight in 85.50..89.50 -> carbohydrateProportion = 9.00f
      inputWeight in 90.00..98.50 -> carbohydrateProportion = 8.00f
      inputWeight in 99.00..107.50 -> carbohydrateProportion = 7.00f
      inputWeight > 108.00 -> carbohydrateProportion = 6.00f
      else -> Toast.makeText(applicationContext, "invalido", Toast.LENGTH_SHORT).show()
    }

    val correctionDose: Float = (inputPreGlucose - inputPostGlucose) / 50.00f
    val mealDose: Float = inputCarbohydrate / carbohydrateProportion
    val totalDose: Float = correctionDose + mealDose
    correctionResult.text = getString(R.string.str_correction_dose, "$correctionDose")
    mealResult.text = getString(R.string.str_meal_dose, "$mealDose")
    totalResult.text = getString(R.string.str_total_dose, "$totalDose")
    saveSharedPreference(totalDose)
  }

  fun saveSharedPreference(result: Float) {
    val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
    with (sharedPref.edit()) {
      putFloat(getString(R.string.saved_calc_insulin), result)
      apply()
    }
  }
  fun getSharedPred(): Float {
    val sharedPref = getPreferences(Context.MODE_PRIVATE)
    return sharedPref.getFloat(getString(R.string.saved_calc_insulin), 0.0f)
  }

  private fun setupCachedResult() {
    val calcValue = getSharedPred()
    totalResult.text = calcValue.toString()
  }

}