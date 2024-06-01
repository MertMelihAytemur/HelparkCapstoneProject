package com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model

import com.tr.helpark.helparkcapstoneproject.core.base.BaseSpinnerItem

/**
 * Expire date list item ui model for [ExpireDateSpinnerAdapter]
 */
data class ExpireDateListItemUiModel(
    override val name: String
) : BaseSpinnerItem

data class FuelTypesListItemUiModel(
    override val name: String,
    val id: Int
) : BaseSpinnerItem
