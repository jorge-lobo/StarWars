package com.example.starwarsjunior.ui.personage

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.starwarsjunior.R
import com.example.starwarsjunior.data.personage.objects.Specie
import com.example.starwarsjunior.databinding.RvChecklistBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class SpecieBindingItem(val specie: Specie) : AbstractBindingItem<RvChecklistBinding>() {

    /**
     * defines the type defining this item. must be unique, preferably an id
     *
     * @return the type
     */
    override val type: Int
        get() = R.id.fastadapter_id

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param holder the viewHolder of this item
     */

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): RvChecklistBinding {
        return RvChecklistBinding.inflate(inflater, parent, false)
    }
    override fun bindView(binding: RvChecklistBinding, payloads: List<Any>) {
        binding.cbSpecie.text = specie.name.lowercase()
    }
}