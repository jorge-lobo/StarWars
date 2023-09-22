package com.example.starwarsjunior.ui.ship

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityShipBinding
import com.example.starwarsjunior.ui.ship.details.ShipDetailActivity

class ShipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShipBinding.inflate(layoutInflater)
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