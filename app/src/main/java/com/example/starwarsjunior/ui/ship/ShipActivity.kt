package com.example.starwarsjunior.ui.ship

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
import com.example.starwarsjunior.data.ship.objects.Ship
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

    private lateinit var launcher: ActivityResultLauncher<Intent>

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
        binding.searchBox.addTextChangedListener(object : TextWatcher {
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

        //when returning from ShipDetail, this function will load preview sort list values
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    mShipViewModel.updateSortedShips()
                }
            }

        fastItemAdapter.onClickListener = { _, _, item, _ ->
            val intent = Intent(this, ShipDetailActivity::class.java)
            val idFromUrl = Utils.extractIdFromUrl(item.ship.url)
            intent.putExtra(ShipDetailActivity.EXTRA_SHIP_ID, idFromUrl)
            launcher.launch(intent)
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
                updateShipAdapter(shipList)
            }
        )

        //list with filtered ships
        mShipViewModel.filteredShips.observe(
            this,
            Observer { shipList ->
                updateShipAdapter(shipList)
            }
        )

        //toggle the visibility of the no results message,
        //applied both in the search bar and in the filters
        mShipViewModel.resultsNotFoundMessage.observe(this, Observer { notFound ->
            if (notFound) {
                binding.resultsNotFound.visibility = View.VISIBLE
            } else {
                binding.resultsNotFound.visibility = View.GONE
            }
        })

        binding.resetSearchButton.setOnClickListener {
            binding.searchBox.setText("")
            mShipViewModel.applyFilters()
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

    private fun updateShipAdapter(shipList: List<Ship>?) {
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
}