package com.example.starwarsjunior.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityMainBinding
import com.example.starwarsjunior.ui.characters.CharactersListActivity
import com.example.starwarsjunior.ui.planets.PlanetsListActivity
import com.example.starwarsjunior.ui.ships.ShipsListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.charactersButton.setOnClickListener {
            startActivity(Intent(this, CharactersListActivity::class.java))
        }

        binding.planetsButton.setOnClickListener {
            startActivity(Intent(this, PlanetsListActivity::class.java))
        }

        binding.shipsButton.setOnClickListener {
            startActivity(Intent(this, ShipsListActivity::class.java))
        }
    }
}