package com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model

import android.content.Context
import android.content.res.Resources
import com.tr.helpark.helparkcapstoneproject.common.customview.BaseHintSpinnerAdapter

/**
 * Spinner Adapter for expire date(month and year) for credit card
 */
class ExpireDateSpinnerAdapter(
    context: Context,
    resource: Int,
    baseList: List<ExpireDateListItemUiModel>,
    resources: Resources
) : BaseHintSpinnerAdapter<ExpireDateListItemUiModel>(context, resource, baseList, resources) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        addAll()
    }

    override fun getItemGivenPosition(position: Int):
        ExpireDateListItemUiModel? = if (baseList.size > position) baseList[position] else null
}


class FuelTypeSpinnerAdapter(
    context: Context,
    resource: Int,
    baseList: List<FuelTypesListItemUiModel>,
    resources: Resources
) : BaseHintSpinnerAdapter<FuelTypesListItemUiModel>(context, resource, baseList, resources) {

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        addAll()
    }

    override fun getItemGivenPosition(position: Int):
            FuelTypesListItemUiModel? = if (baseList.size > position) baseList[position] else null
}