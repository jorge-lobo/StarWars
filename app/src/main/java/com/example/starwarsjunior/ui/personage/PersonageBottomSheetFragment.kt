package com.example.starwarsjunior.ui.personage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsjunior.R
import com.example.starwarsjunior.databinding.FragmentBottomSheetListDialogItemBinding
import com.example.starwarsjunior.databinding.FragmentPersonageBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Customize parameter argument names
const val ARG_ITEM_COUNT = "item_count"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    BottomSheetFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class BottomSheetFragment(private val mainViewModel: PersonageViewModel) : BottomSheetDialogFragment() {

    private var _binding: FragmentPersonageBottomSheetDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPersonageBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<RecyclerView>(R.id.list)?.layoutManager =
            LinearLayoutManager(context)
        activity?.findViewById<RecyclerView>(R.id.list)?.adapter =
            arguments?.getInt(ARG_ITEM_COUNT)?.let { ItemAdapter(it) }

        //Filter buttons
        binding.maleButton.isChecked = mainViewModel.isFilterSelected("male")
        binding.femaleButton.isChecked = mainViewModel.isFilterSelected("female")
        binding.undefinedButton.isChecked = mainViewModel.isFilterSelected("n/a")

        binding.maleButton.setOnClickListener {
            mainViewModel.toggleFilter("male")
            binding.maleButton.isChecked = mainViewModel.isFilterSelected("male")
        }

        binding.femaleButton.setOnClickListener {
            mainViewModel.toggleFilter("female")
            binding.femaleButton.isChecked = mainViewModel.isFilterSelected("female")
        }

        binding.undefinedButton.setOnClickListener {
            mainViewModel.toggleFilter("n/a")
            binding.undefinedButton.isChecked = mainViewModel.isFilterSelected("n/a")
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
        fun newInstance(itemCount: Int, mainViewModel: PersonageViewModel): BottomSheetFragment =
            BottomSheetFragment(mainViewModel).apply {
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