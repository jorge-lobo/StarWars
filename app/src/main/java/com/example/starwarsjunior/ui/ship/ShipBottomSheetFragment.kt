package com.example.starwarsjunior.ui.ship

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
import com.example.starwarsjunior.databinding.FragmentShipBottomSheetListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ShipBottomSheetFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class ShipBottomSheetFragment(private val mainViewModel: ShipViewModel) :
    BottomSheetDialogFragment() {

    private var _binding: FragmentShipBottomSheetListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShipBottomSheetListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<RecyclerView>(R.id.list)?.layoutManager =
            LinearLayoutManager(context)
        activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
            arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }

        //Filter buttons

        //filter by hyperdriveRating
        binding.slowButton.isChecked = mainViewModel.isFilterSelected("slow")
        binding.averageButton.isChecked = mainViewModel.isFilterSelected("average")
        binding.fastButton.isChecked = mainViewModel.isFilterSelected("fast")

        binding.slowButton.setOnClickListener {
            mainViewModel.toggleFilter("slow")
            binding.slowButton.isChecked = mainViewModel.isFilterSelected("slow")
        }

        binding.averageButton.setOnClickListener {
            mainViewModel.toggleFilter("average")
            binding.averageButton.isChecked = mainViewModel.isFilterSelected("average")
        }

        binding.fastButton.setOnClickListener {
            mainViewModel.toggleFilter("fast")
            binding.fastButton.isChecked = mainViewModel.isFilterSelected("fast")
        }

        //filter by crew
        binding.littleButton.isChecked = mainViewModel.isFilterSelected("little")
        binding.mediumButton.isChecked = mainViewModel.isFilterSelected("medium")
        binding.largeButton.isChecked = mainViewModel.isFilterSelected("large")

        binding.littleButton.setOnClickListener {
            mainViewModel.toggleFilter("little")
            binding.littleButton.isChecked = mainViewModel.isFilterSelected("little")
        }

        binding.mediumButton.setOnClickListener {
            mainViewModel.toggleFilter("medium")
            binding.mediumButton.isChecked = mainViewModel.isFilterSelected("medium")
        }

        binding.largeButton.setOnClickListener {
            mainViewModel.toggleFilter("large")
            binding.largeButton.isChecked = mainViewModel.isFilterSelected("large")
        }

        //Reset button
        binding.reset.setOnClickListener {
            binding.crewButtons.forEach { view ->
                if (view is ToggleButton) {
                    view.isChecked = false
                }
            }
            binding.hyperdriveButtons.forEach { view ->
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
        fun newInstance(itemCount: Int, mainViewModel: ShipViewModel): ShipBottomSheetFragment =
            ShipBottomSheetFragment(mainViewModel).apply {
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