package com.example.starwarsjunior.ui.planet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsjunior.R
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
        val searchBox = findViewById<EditText>(R.id.search_box)

        searchBox.addTextChangedListener(object : TextWatcher {
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

        fastItemAdapter.onClickListener = { _, _, item, _ ->
            val intent = Intent(this, PlanetDetailActivity::class.java)
            val idFromUrl = Utils.extractIdFromUrl(item.planet.url)
            intent.putExtra(PlanetDetailActivity.EXTRA_PLANET_ID, idFromUrl)
            startActivity(intent)
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

                val items = ArrayList<PlanetBindingItem>()
                mPlanetItemAdapter.clear()

                items.clear()
                if (planetList != null) {
                    for (planet in planetList) {
                        val item = PlanetBindingItem(planet)
                        items.add(item)
                    }
                    mPlanetItemAdapter.add(items)
                }
            }
        )

        //list with filtered planets
        mPlanetViewModel.filteredPlanets.observe(
            this,
            Observer { planetList ->

                val items = ArrayList<PlanetBindingItem>()
                mPlanetItemAdapter.clear()

                items.clear()
                if (planetList != null) {
                    for (planet in planetList) {
                        val item = PlanetBindingItem(planet)
                        items.add(item)
                    }
                    mPlanetItemAdapter.add(items)
                }
            }
        )

        binding.resetButton.setOnClickListener {
            binding.searchBox.setText("")
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
}