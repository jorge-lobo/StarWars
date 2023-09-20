package com.example.starwarsjunior.ui.planets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityPlanetDetailBinding

class PlanetDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanetDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanetDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }




    }
}