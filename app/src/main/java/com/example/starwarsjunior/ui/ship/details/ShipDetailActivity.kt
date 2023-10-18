package com.example.starwarsjunior.ui.ship.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.starwarsjunior.R
import com.example.starwarsjunior.databinding.ActivityShipDetailBinding

class ShipDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipDetailBinding
    private lateinit var mShipDetailViewModel: ShipDetailViewModel

    companion object {

        const val EXTRA_SHIP_ID: String = "extra_ship_id"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mShipDetailViewModel = ViewModelProvider(this).get(ShipDetailViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ship_detail)

        val shipID = intent.getIntExtra(EXTRA_SHIP_ID, 9999)

        binding.viewModel = mShipDetailViewModel

        //We need to use this in case we are using MutableLiveData in our ModelView and we want to update state from the ModelView
        binding.lifecycleOwner = this

        mShipDetailViewModel.initialize(shipID)

        binding.backButton.setOnClickListener {
            //to load preview sort list values
            val returnIntent = Intent()
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }
}