package com.example.starwarsjunior.ui.planets

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityPlanetsListBinding
class PlanetsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanetsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanetsListBinding.inflate(layoutInflater)
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

