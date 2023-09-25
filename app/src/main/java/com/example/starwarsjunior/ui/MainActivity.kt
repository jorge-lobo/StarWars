package com.example.starwarsjunior.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityMainBinding
import com.example.starwarsjunior.ui.personage.PersonageActivity
import com.example.starwarsjunior.ui.planet.PlanetActivity
import com.example.starwarsjunior.ui.ship.ShipActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.personagesButton.setOnClickListener {
            startActivity(Intent(this, PersonageActivity::class.java))
        }

        binding.planetsButton.setOnClickListener {
            startActivity(Intent(this, PlanetActivity::class.java))
        }

        binding.shipsButton.setOnClickListener {
            startActivity(Intent(this, ShipActivity::class.java))
        }
    }
}