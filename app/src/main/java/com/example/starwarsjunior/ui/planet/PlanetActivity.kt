package com.example.starwarsjunior.ui.planet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityPlanetBinding
import com.example.starwarsjunior.ui.planet.details.PlanetDetailActivity

class PlanetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //só para testar enquanto não tenho a lista
        binding.starWarsLogoSmall.setOnClickListener {
            startActivity(Intent(this, PlanetDetailActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            finish()
        }


    }

}

