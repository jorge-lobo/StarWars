package com.example.starwarsjunior.ui.personage

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsjunior.R
import com.example.starwarsjunior.databinding.ActivityPersonageBinding
import com.example.starwarsjunior.ui.personage.details.PersonageDetailActivity
import com.example.starwarsjunior.utils.Utils
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension

class PersonageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonageBinding
    lateinit var personageBottomSheetButton: Button
    lateinit var bottomSheetFragment: BottomSheetFragment

    private lateinit var mPersonageViewModel : PersonageViewModel

    private val mPersonageItemAdapter = FastItemAdapter<PersonageBindingItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mViewModel = ViewModelProvider(this).get(PersonageViewModel::class.java)
        mPersonageViewModel = mViewModel as PersonageViewModel

        binding = DataBindingUtil.setContentView(this, R.layout.activity_personage)


        binding.viewModel = mPersonageViewModel
        // This makes it possible to use @OnLifecycleEvent(Lifecycle.Event.ON_START) inside our ModelView
        // without having it to call our MV getUsers from the onStart fragment method
        this.lifecycle.addObserver(mPersonageViewModel)
        // We need to use this in case we are using MutableLiveData in our ModelView and we want to update state from the modelview
        binding.lifecycleOwner = this

        //FastAdapter configuration
        val fastItemAdapter = FastAdapter.with(mPersonageItemAdapter)

        fastItemAdapter.setHasStableIds(true)
        val selectExtension = fastItemAdapter.getSelectExtension()
        selectExtension.isSelectable = true

        fastItemAdapter.onClickListener = { _, _, item, _ ->
            val intent = Intent(this, PersonageDetailActivity::class.java)
            val idFromUrl = Utils.extractIdFromUrl(item.personage.url)
            intent.putExtra(PersonageDetailActivity.EXTRA_PERSONAGE_ID, idFromUrl)
            startActivity(intent)
            false
        }

        binding.personagesListRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = fastItemAdapter
        }

        //Observers for when variables change in the viewModel and need some code
        mPersonageViewModel.personages.observe(
            this,
            Observer { personageList ->

                val items = ArrayList<PersonageBindingItem>()
                mPersonageItemAdapter.clear()

                items.clear()
                if (personageList != null) {
                    for (personage in personageList) {
                        val item = PersonageBindingItem(personage)
                        items.add(item)
                    }
                    mPersonageItemAdapter.add(items)
                }
            }
        )


        //só para testar enquanto não tenho a lista
        binding.starWarsLogoSmall.setOnClickListener {
            startActivity(Intent(this, PersonageDetailActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            finish()
        }


        binding.resetButton.setOnClickListener {
            binding.searchBox.setText("")
        }

        //fragment bottom sheet
        binding.personageBottomSheetButton.setOnClickListener {
            bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, "BSDialogFragment")

        }

        binding.ascButton.setOnClickListener {
            binding.ascButton.setImageResource(R.drawable.arrow_up_yellow_group)
            binding.descButton.setImageResource(R.drawable.arrow_down_white_group)
            binding.ascButton.isClickable = false
            binding.descButton.isClickable = true
        }

        binding.descButton.setOnClickListener {
            binding.ascButton.setImageResource(R.drawable.arrow_up_white_group)
            binding.descButton.setImageResource(R.drawable.arrow_down_yellow_group)
            binding.ascButton.isClickable = true
            binding.descButton.isClickable = false
        }

        binding.nameButton.setOnClickListener {
            binding.nameButton.isClickable = false
            binding.yearButton.isClickable = true
            binding.yearButton.isChecked = false
        }

        binding.yearButton.setOnClickListener {
            binding.yearButton.isClickable = false
            binding.nameButton.isClickable = true
            binding.nameButton.isChecked = false
        }

    }

}