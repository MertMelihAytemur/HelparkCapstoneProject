package com.tr.helpark.helparkcapstoneproject.common.util

import com.google.android.gms.maps.model.Marker

/**
 * Created by tasci on 30.12.2023.
 */
sealed interface UiActions {

    interface Home{

        fun collapseBottomSheet()

        fun expandBottomSheet()

        fun setHelperViewsVisibility(enabled: Boolean)

        fun onMarkerClickAction(marker: Marker)
    }

}