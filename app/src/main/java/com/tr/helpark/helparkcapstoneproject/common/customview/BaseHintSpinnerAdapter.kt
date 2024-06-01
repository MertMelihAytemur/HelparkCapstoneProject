package com.tr.helpark.helparkcapstoneproject.common.customview

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.core.base.BaseSpinnerItem

/**
 * Base Hint Spinner Adapter
 * first item must be hint
 */
abstract class BaseHintSpinnerAdapter<T : BaseSpinnerItem>(
    context: Context,
    resource: Int,
    protected var baseList: List<T>,
    private val resources: Resources
) :
    ArrayAdapter<String>(context, resource, baseList.map { t -> t.name }) {

    /** Disable the first item from Spinner
     First item will be use for hint
     **/

    override fun isEnabled(position: Int):
        Boolean = position != 0

    override fun getPosition(item: String?):
        Int = 0.coerceAtLeast(super.getPosition(item) - 1)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        if (position == 0) {
            (view as TextView).setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.text_color_black,
                    null
                )
            )
        } else {
            // here is it possible to define color for other items by
            (view as TextView).setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.text_color_black,
                    null
                )
            )
        }
        return super.getView(position, convertView, parent)
    }

    /**
     * Updates Spinner Adapter List with given list of items
     * and set the Hint for the Adapter
     */
    @Suppress("UNCHECKED_CAST")
    fun updateList(list: List<T>) {
        clear()
        val updatedList = list
            .map { model -> model.name }
            .toMutableList()
        updatedList.add(0, "Seçiniz")
        baseList = list
        addAll(updatedList)
        notifyDataSetChanged()
    }

    /**
     * returns the item [T] on given [position]
     * custom getter for [baseList]
     */
    abstract fun getItemGivenPosition(position: Int): T?

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view: TextView =
            super.getDropDownView(position, convertView, parent) as TextView
        // set the color of first item in the drop down list to gray
        if (position == 0) {
            view.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.text_color_black,
                    null
                )
            )
        } else {
            // here is it possible to define color for other items by
            view.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.text_color_black,
                    null
                )
            )
        }
        return view
    }
}
