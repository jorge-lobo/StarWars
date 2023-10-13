package com.example.starwarsjunior.ui.planet.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.starwarsjunior.R
import com.example.starwarsjunior.databinding.ActivityPlanetDetailBinding

class PlanetDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanetDetailBinding
    private lateinit var mPlanetDetailViewModel: PlanetDetailViewModel

    companion object {

        const val EXTRA_PLANET_ID: String = "extra_planet_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPlanetDetailViewModel =
            ViewModelProvider(this).get(PlanetDetailViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_planet_detail)

        val planetID = intent.getIntExtra(EXTRA_PLANET_ID, 9999)

        binding.viewModel = mPlanetDetailViewModel

        //We need to use this in case we are using MutableLiveData in our ModelView and we want to update state from the ModelView
        binding.lifecycleOwner = this

        mPlanetDetailViewModel.initialize(planetID)

        binding.backButton.setOnClickListener {
            finish()
        }




    }
}