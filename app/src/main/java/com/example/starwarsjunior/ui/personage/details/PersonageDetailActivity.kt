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

        const val EXTRA_PERSONAGE_ID : String = "extra_personage_id"
        private val PERSONAGE_ID = "personage_id"

        fun newInstance(personageID: Int) : PersonageDetailActivity {
            val personageDetailActivity = PersonageDetailActivity()

            val args = Bundle()
            args.putInt(PERSONAGE_ID, personageID)
            personageDetailActivity.setArguments(args)

            return personageDetailActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val personageID = intent.getIntExtra(EXTRA_PERSONAGE_ID, 9999)

        mViewModel = ViewModelProvider(this).get(PersonageDetailViewModel::class.java)
        mPersonageDetailViewModel = mViewModel as PersonageDetailViewModel

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_personage_detail, container, false)

        binding.viewModel = mPersonageDetailViewModel

        // We need to use this in case we are using MutableLiveData in our ModelView and we want to update state from the modelView
        binding.lifecycleOwner = this

        personageID = requireArguments().getInt(PERSONAGE_ID)

        mPersonageDetailViewModel.initialize(personageID)

    }

}