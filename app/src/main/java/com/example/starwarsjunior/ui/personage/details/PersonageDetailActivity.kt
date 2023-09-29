package com.example.starwarsjunior.ui.personage.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.starwarsjunior.R
import com.example.starwarsjunior.databinding.ActivityPersonageDetailBinding

class PersonageDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonageDetailBinding
    private lateinit var mPersonageDetailViewModel: PersonageDetailViewModel

    companion object {

        const val EXTRA_PERSONAGE_ID: String = "extra_personage_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPersonageDetailViewModel =
            ViewModelProvider(this).get(PersonageDetailViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_personage_detail)

        val personageID = intent.getIntExtra(EXTRA_PERSONAGE_ID, 9999)

        binding.viewModel = mPersonageDetailViewModel

        // We need to use this in case we are using MutableLiveData in our ModelView and we want to update state from the modelView
        binding.lifecycleOwner = this

        mPersonageDetailViewModel.initialize(personageID)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}