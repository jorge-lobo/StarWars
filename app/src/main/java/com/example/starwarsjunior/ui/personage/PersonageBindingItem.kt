package com.example.starwarsjunior.ui.personage

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.starwarsjunior.R
import com.example.starwarsjunior.data.personage.objects.Personage
import com.mikepenz.fastadapter.items.AbstractItem

class PersonageBindingItem(val personage: Personage) : AbstractItem<PersonageBindingItem.ViewHolder>() {

    /**
     * defines the type defining this item. must be unique, preferably an id
     *
     * @return the type
     */
    override val type: Int
        get() = R.id.fastadapter_id

    /**
     * defines the layout wich will be used for this item in the list
     *
     * @return the layout for this item
     */
    override val layoutRes: Int
        get() = R.layout.personage_item

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param holder the viewHolder of this item
     */
    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.name.text = personage.name
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        holder.name.text = null
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    /**
     * our ViewHolder
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var name: TextView = view.findViewById(R.id.personage_name)
    }
}