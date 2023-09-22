package com.example.starwarsjunior.ui.personage.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityPersonageDetailBinding

class PersonageDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonageDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }



    }
    companion object {
        const val EXTRA_PERSONAGE_ID = "extra_personage_id"
    }
}