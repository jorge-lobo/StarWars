package com.example.starwarsjunior.ui.ship

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
import com.example.starwarsjunior.databinding.ActivityShipBinding
import com.example.starwarsjunior.ui.ship.details.ShipDetailActivity
import com.example.starwarsjunior.utils.Utils
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension

class ShipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipBinding
    private lateinit var shipBottomSheetFragment: ShipBottomSheetFragment

    private lateinit var mShipViewModel: ShipViewModel

    private val mShipItemAdapter = FastItemAdapter<ShipBindingItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mShipViewModel = ViewModelProvider(this).get(ShipViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_ship)

        binding.viewModel = mShipViewModel

        this.lifecycle.addObserver(mShipViewModel)
        binding.lifecycleOwner = this

        if (mShipViewModel.isDataPreloaded()) {
            mShipViewModel.onStart()
        } else {
            mShipViewModel.getCachedShips(true)
        }

        //search box
        val searchBox = findViewById<EditText>(R.id.search_box)

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mShipViewModel.searchQuery.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //FastAdapter configuration
        val fastItemAdapter = FastAdapter.with(mShipItemAdapter)

        fastItemAdapter.setHasStableIds(true)
        val selectExtension = fastItemAdapter.getSelectExtension()
        selectExtension.isSelectable = true

        fastItemAdapter.onClickListener = { _, _, item, _ ->
            val intent = Intent(this, ShipDetailActivity::class.java)
            val idFromUrl = Utils.extractIdFromUrl(item.ship.url)
            intent.putExtra(ShipDetailActivity.EXTRA_SHIP_ID, idFromUrl)
            startActivity(intent)
            binding.searchBox.setText("")
            false
        }

        binding.shipsListRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = fastItemAdapter
        }

        //Observers for when variables change in the viewModel and need some code
        mShipViewModel.sortedShips.observe(
            this,
            Observer { shipList ->

                val items = ArrayList<ShipBindingItem>()
                mShipItemAdapter.clear()

                items.clear()
                if (shipList != null) {
                    for (ship in shipList) {
                        val item = ShipBindingItem(ship)
                        items.add(item)
                    }
                    mShipItemAdapter.add(items)
                }

            }
        )

        //list with filtered ships
        mShipViewModel.filteredShips.observe(
            this,
            Observer { shipList ->

                val items = ArrayList<ShipBindingItem>()
                mShipItemAdapter.clear()

                items.clear()
                if (shipList != null) {
                    for (ship in shipList) {
                        val item = ShipBindingItem(ship)
                        items.add(item)
                    }
                    mShipItemAdapter.add(items)
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
        binding.shipBottomSheetButton.setOnClickListener {
            shipBottomSheetFragment = ShipBottomSheetFragment(mShipViewModel)
            shipBottomSheetFragment.show(supportFragmentManager, "BSDialogFragment")
        }

        //Order buttons
        binding.upButton.setOnClickListener {
            binding.upButton.setImageResource(R.drawable.arrow_up_yellow_group)
            binding.downButton.setImageResource(R.drawable.arrow_down_white_group)
            binding.upButton.isClickable = false
            binding.downButton.isClickable = true

            if (binding.sortNameButton.isChecked) {
                mShipViewModel.toggleSortNameOrder()
            } else {
                mShipViewModel.toggleSortLengthOrder()
            }
        }

        binding.downButton.setOnClickListener {
            binding.upButton.setImageResource(R.drawable.arrow_up_white_group)
            binding.downButton.setImageResource(R.drawable.arrow_down_yellow_group)
            binding.upButton.isClickable = true
            binding.downButton.isClickable = false

            if (binding.sortNameButton.isChecked) {
                mShipViewModel.toggleSortNameOrder()
            } else {
                mShipViewModel.toggleSortLengthOrder()
            }
        }

        binding.sortNameButton.setOnClickListener {
            binding.sortNameButton.isClickable = false
            binding.sortLengthButton.isClickable = true
            binding.sortLengthButton.isChecked = false

            mShipViewModel.toggleSortNameOrder()
        }

        binding.sortLengthButton.setOnClickListener {
            binding.sortLengthButton.isClickable = false
            binding.sortNameButton.isClickable = true
            binding.sortNameButton.isChecked = false

            mShipViewModel.toggleSortLengthOrder()
        }

    }
}