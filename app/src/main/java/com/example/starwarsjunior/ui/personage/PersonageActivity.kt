package com.example.starwarsjunior.ui.personage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsjunior.R
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.databinding.ActivityPersonageBinding
import com.example.starwarsjunior.ui.personage.details.PersonageDetailActivity
import com.example.starwarsjunior.utils.Utils
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension

class PersonageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonageBinding
    private lateinit var personageBottomSheetFragment: PersonageBottomSheetFragment

    private lateinit var mPersonageViewModel: PersonageViewModel

    private val mPersonageItemAdapter = FastItemAdapter<PersonageBindingItem>()

    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPersonageViewModel = ViewModelProvider(this).get(PersonageViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_personage)

        binding.viewModel = mPersonageViewModel
        // This makes it possible to use @OnLifecycleEvent(Lifecycle.Event.ON_START) inside our ModelView
        // without having it to call our MV getUsers from the onStart fragment method
        this.lifecycle.addObserver(mPersonageViewModel)
        // We need to use this in case we are using MutableLiveData in our ModelView and we want to update state from the modelview
        binding.lifecycleOwner = this

        if (mPersonageViewModel.isDataPreloaded()) {
            mPersonageViewModel.onStart()
        } else {
            mPersonageViewModel.getCachedPersonages(true)
        }

        //search box
        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mPersonageViewModel.searchQuery.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //FastAdapter configuration
        val fastItemAdapter = FastAdapter.with(mPersonageItemAdapter)

        fastItemAdapter.setHasStableIds(true)
        val selectExtension = fastItemAdapter.getSelectExtension()
        selectExtension.isSelectable = true

        //when returning from PersonageDetail, this function will load preview sort list values
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    mPersonageViewModel.updateSortedPersonages()
                }
            }

        fastItemAdapter.onClickListener = { _, _, item, _ ->
            val intent = Intent(this, PersonageDetailActivity::class.java)
            val idFromUrl = Utils.extractIdFromUrl(item.personage.url)
            intent.putExtra(PersonageDetailActivity.EXTRA_PERSONAGE_ID, idFromUrl)
            launcher.launch(intent)
            binding.searchBox.setText("")
            false
        }

        binding.personagesListRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = fastItemAdapter
        }

        //Observers for when variables change in the viewModel and need some code
        mPersonageViewModel.sortedPersonages.observe(
            this,
            Observer { personageList ->
                updatePersonageAdapter(personageList)
            }
        )

        //list with filtered personages
        mPersonageViewModel.filteredPersonages.observe(
            this,
            Observer { personageList ->
                updatePersonageAdapter(personageList)
            }
        )

        //toggle the visibility of the no results message
        //applied both in the search bar and in the filters
        mPersonageViewModel.resultsNotFoundMessage.observe(this, Observer { notFound ->
            if (notFound) {
                binding.resultsNotFound.visibility = View.VISIBLE
            } else {
                binding.resultsNotFound.visibility = View.GONE
            }
        })

        binding.resetButton.setOnClickListener {
            binding.searchBox.setText("")
            mPersonageViewModel.applyFilters()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        //fragment bottom sheet
        binding.personageBottomSheetButton.setOnClickListener {
            personageBottomSheetFragment = PersonageBottomSheetFragment(mPersonageViewModel)
            personageBottomSheetFragment.show(supportFragmentManager, "BSDialogFragment")

        }

        //Order buttons
        binding.upButton.setOnClickListener {
            binding.upButton.setImageResource(R.drawable.arrow_up_yellow_group)
            binding.downButton.setImageResource(R.drawable.arrow_down_white_group)
            binding.upButton.isClickable = false
            binding.downButton.isClickable = true

            if (binding.sortNameButton.isChecked) {
                mPersonageViewModel.toggleSortNameOrder()
            } else {
                mPersonageViewModel.toggleSortYearOrder()
            }
        }

        binding.downButton.setOnClickListener {
            binding.upButton.setImageResource(R.drawable.arrow_up_white_group)
            binding.downButton.setImageResource(R.drawable.arrow_down_yellow_group)
            binding.upButton.isClickable = true
            binding.downButton.isClickable = false

            if (binding.sortNameButton.isChecked) {
                mPersonageViewModel.toggleSortNameOrder()
            } else {
                mPersonageViewModel.toggleSortYearOrder()
            }
        }

        binding.sortNameButton.setOnClickListener {
            binding.sortNameButton.isClickable = false
            binding.sortYearButton.isClickable = true
            binding.sortYearButton.isChecked = false

            mPersonageViewModel.toggleSortNameOrder()
        }

        binding.sortYearButton.setOnClickListener {
            binding.sortYearButton.isClickable = false
            binding.sortNameButton.isClickable = true
            binding.sortNameButton.isChecked = false

            mPersonageViewModel.toggleSortYearOrder()
        }
    }

    private fun updatePersonageAdapter(personageList: List<Personage>?) {
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
}