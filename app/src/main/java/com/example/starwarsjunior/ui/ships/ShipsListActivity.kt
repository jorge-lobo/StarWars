package com.example.starwarsjunior.ui.ships

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityShipsListBinding

class ShipsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShipsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //só para testar enquanto não tenho a lista
        binding.starWarsLogoSmall.setOnClickListener {
            startActivity(Intent(this, ShipDetailActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }
}