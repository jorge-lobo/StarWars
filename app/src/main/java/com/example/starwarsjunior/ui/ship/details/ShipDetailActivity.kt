package com.example.starwarsjunior.ui.ship.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityShipDetailBinding

class ShipDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipDetailBinding

    companion object {

        const val EXTRA_SHIP_ID: String = "extra_ship_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.backButton.setOnClickListener {
            finish()
        }




    }
}