package com.example.starwarsjunior.ui.planet

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
import com.example.starwarsjunior.data.planet.objects.Planet
import com.example.starwarsjunior.databinding.ActivityPlanetBinding
import com.example.starwarsjunior.ui.planet.details.PlanetDetailActivity
import com.example.starwarsjunior.utils.Utils
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension

class PlanetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlanetBinding
    private lateinit var planetBottomSheetFragment: PlanetBottomSheetFragment

    private lateinit var mPlanetViewModel: PlanetViewModel

    private val mPlanetItemAdapter = FastItemAdapter<PlanetBindingItem>()

    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPlanetViewModel = ViewModelProvider(this).get(PlanetViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_planet)

        binding.viewModel = mPlanetViewModel

        this.lifecycle.addObserver(mPlanetViewModel)
        binding.lifecycleOwner = this

        if (mPlanetViewModel.isDataPreloaded()) {
            mPlanetViewModel.onStart()
        } else {
            mPlanetViewModel.getCachedPlanets(true)
        }

        //search box
        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mPlanetViewModel.searchQuery.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //FastAdapter configuration
        val fastItemAdapter = FastAdapter.with(mPlanetItemAdapter)

        fastItemAdapter.setHasStableIds(true)
        val selectExtension = fastItemAdapter.getSelectExtension()
        selectExtension.isSelectable = true

        //when returning from PlanetDetail, this function will load preview sort list values
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    mPlanetViewModel.updateSortedPlanets()
                }
            }

        fastItemAdapter.onClickListener = { _, _, item, _ ->
            val intent = Intent(this, PlanetDetailActivity::class.java)
            val idFromUrl = Utils.extractIdFromUrl(item.planet.url)
            intent.putExtra(PlanetDetailActivity.EXTRA_PLANET_ID, idFromUrl)
            launcher.launch(intent)
            binding.searchBox.setText("")
            false
        }

        binding.planetsListRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = fastItemAdapter
        }

        //Observers for when variables change in the viewModel and need some code
        mPlanetViewModel.sortedPlanets.observe(
            this,
            Observer { planetList ->
                updatePlanetAdapter(planetList)
            }
        )

        //list with filtered planets
        mPlanetViewModel.filteredPlanets.observe(
            this,
            Observer { planetList ->
                updatePlanetAdapter(planetList)
            }
        )

        //toggle the visibility of the no results message,
        //applied both in the search bar and in the filters
        mPlanetViewModel.resultsNotFoundMessage.observe(this, Observer { notFound ->
            if (notFound) {
                binding.resultsNotFound.visibility = View.VISIBLE
            } else {
                binding.resultsNotFound.visibility = View.GONE
            }
        })

        binding.resetSearchButton.setOnClickListener {
            binding.searchBox.setText("")
            mPlanetViewModel.applyFilters()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        //fragment bottom sheet
        binding.planetBottomSheetButton.setOnClickListener {
            planetBottomSheetFragment = PlanetBottomSheetFragment(mPlanetViewModel)
            planetBottomSheetFragment.show(supportFragmentManager, "BSDialogFragment")

        }

        //Order buttons
        binding.upButton.setOnClickListener {
            binding.upButton.setImageResource(R.drawable.arrow_up_yellow_group)
            binding.downButton.setImageResource(R.drawable.arrow_down_white_group)
            binding.upButton.isClickable = false
            binding.downButton.isClickable = true

            if (binding.sortNameButton.isChecked) {
                mPlanetViewModel.toggleSortNameOrder()
            } else {
                mPlanetViewModel.toggleSortedSizeOrder()
            }
        }

        binding.downButton.setOnClickListener {
            binding.upButton.setImageResource(R.drawable.arrow_up_white_group)
            binding.downButton.setImageResource(R.drawable.arrow_down_yellow_group)
            binding.upButton.isClickable = true
            binding.downButton.isClickable = false

            if (binding.sortNameButton.isChecked) {
                mPlanetViewModel.toggleSortNameOrder()
            } else {
                mPlanetViewModel.toggleSortedSizeOrder()
            }
        }

        binding.sortNameButton.setOnClickListener {
            binding.sortNameButton.isClickable = false
            binding.sortSizeButton.isClickable = true
            binding.sortSizeButton.isChecked = false

            mPlanetViewModel.toggleSortNameOrder()
        }

        binding.sortSizeButton.setOnClickListener {
            binding.sortSizeButton.isClickable = false
            binding.sortNameButton.isClickable = true
            binding.sortNameButton.isChecked = false

            mPlanetViewModel.toggleSortedSizeOrder()
        }

    }

    private fun updatePlanetAdapter(planetList: List<Planet>?) {
        val items = ArrayList<PlanetBindingItem>()
        mPlanetItemAdapter.clear()
        items.clear()
        if (planetList != null) {
            for (planet in planetList) {
                val item = PlanetBindingItem(planet)
                items.add((item))
            }
            mPlanetItemAdapter.add(items)
        }
    }
}