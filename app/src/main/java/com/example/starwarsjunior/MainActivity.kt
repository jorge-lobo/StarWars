package com.example.starwarsjunior

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchModeButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.imageLightMode.visibility = ImageView.VISIBLE
                binding.imageDarkMode.visibility = ImageView.INVISIBLE

            } else {
                binding.imageLightMode.visibility = ImageView.INVISIBLE
                binding.imageDarkMode.visibility = ImageView.VISIBLE
            }
        }

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