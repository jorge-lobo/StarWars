package com.example.starwarsjunior.ui.planet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsjunior.R
import com.example.starwarsjunior.databinding.FragmentBottomSheetListDialogItemBinding
import com.example.starwarsjunior.databinding.FragmentPlanetBottomSheetListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    PlanetBottomSheetFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class PlanetBottomSheetFragment(private val mainViewModel: PlanetViewModel) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentPlanetBottomSheetListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlanetBottomSheetListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<RecyclerView>(R.id.list)?.layoutManager =
            LinearLayoutManager(context)
        activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
            arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }

        //Filter buttons

        //filter by climate
        binding.aridButton.isChecked = mainViewModel.isFilterSelected("arid")
        binding.articButton.isChecked = mainViewModel.isFilterSelected("artic")
        binding.hotButton.isChecked = mainViewModel.isFilterSelected("hot")
        binding.temperateButton.isChecked = mainViewModel.isFilterSelected("temperate")
        binding.tropicalButton.isChecked = mainViewModel.isFilterSelected("tropical")
        binding.windyButton.isChecked = mainViewModel.isFilterSelected("windy")

        binding.aridButton.setOnClickListener {
            mainViewModel.toggleFilter("arid")
            binding.aridButton.isChecked = mainViewModel.isFilterSelected("arid")
        }

        binding.articButton.setOnClickListener {
            mainViewModel.toggleFilter("artic")
            binding.articButton.isChecked = mainViewModel.isFilterSelected("artic")
        }

        binding.hotButton.setOnClickListener {
            mainViewModel.toggleFilter("hot")
            binding.hotButton.isChecked = mainViewModel.isFilterSelected("hot")
        }

        binding.temperateButton.setOnClickListener {
            mainViewModel.toggleFilter("temperate")
            binding.temperateButton.isChecked = mainViewModel.isFilterSelected("temperate")
        }

        binding.tropicalButton.setOnClickListener {
            mainViewModel.toggleFilter("tropical")
            binding.tropicalButton.isChecked = mainViewModel.isFilterSelected("tropical")
        }

        binding.windyButton.setOnClickListener {
            mainViewModel.toggleFilter("windy")
            binding.windyButton.isChecked = mainViewModel.isFilterSelected("windy")
        }

        //filter by terrain
        binding.desertButton.isChecked = mainViewModel.isFilterSelected("desert")
        binding.forestButton.isChecked = mainViewModel.isFilterSelected("forest")
        binding.jungleButton.isChecked = mainViewModel.isFilterSelected("jungle")
        binding.mountainButton.isChecked = mainViewModel.isFilterSelected("mountain")
        binding.oceanButton.isChecked = mainViewModel.isFilterSelected("ocean")
        binding.plainsButton.isChecked = mainViewModel.isFilterSelected("plains")

        binding.desertButton.setOnClickListener {
            mainViewModel.toggleFilter("desert")
            binding.desertButton.isChecked = mainViewModel.isFilterSelected("desert")
        }

        binding.forestButton.setOnClickListener {
            mainViewModel.toggleFilter("forest")
            binding.forestButton.isChecked = mainViewModel.isFilterSelected("forest")
        }

        binding.jungleButton.setOnClickListener {
            mainViewModel.toggleFilter("jungle")
            binding.jungleButton.isChecked = mainViewModel.isFilterSelected("jungle")
        }

        binding.mountainButton.setOnClickListener {
            mainViewModel.toggleFilter("mountain")
            binding.mountainButton.isChecked = mainViewModel.isFilterSelected("mountain")
        }

        binding.oceanButton.setOnClickListener {
            mainViewModel.toggleFilter("ocean")
            binding.oceanButton.isChecked = mainViewModel.isFilterSelected("ocean")
        }

        binding.plainsButton.setOnClickListener {
            mainViewModel.toggleFilter("plains")
            binding.plainsButton.isChecked = mainViewModel.isFilterSelected("plains")
        }

        //Reset button
        binding.reset.setOnClickListener {
            binding.climateGroup1.forEach { view ->
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
            binding.climateGroup2.forEach { view ->
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
            binding.terrainGroup1.forEach { view ->
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
            binding.terrainGroup2.forEach { view ->
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
            mainViewModel.resetFilters()
        }

        //SearchButton
        binding.searchButton.setOnClickListener {

            mainViewModel.applyFilters()

            //close BottomSheetFragment
            dismiss()
        }
    }

    private inner class ViewHolder internal constructor(binding: FragmentBottomSheetListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        internal val text: TextView = binding.text
    }

    private inner class ItemAdapter internal constructor(private val mItemCount: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentBottomSheetListDialogItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = position.toString()
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

    companion object {

        // TODO: Customize parameters
        fun newInstance(itemCount: Int, mainViewModel: PlanetViewModel): PlanetBottomSheetFragment =
            PlanetBottomSheetFragment(mainViewModel).apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}