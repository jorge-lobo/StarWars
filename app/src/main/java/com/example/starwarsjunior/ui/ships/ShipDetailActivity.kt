package com.example.starwarsjunior.ui.ships

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityShipDetailBinding

class ShipDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShipDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }




    }
}