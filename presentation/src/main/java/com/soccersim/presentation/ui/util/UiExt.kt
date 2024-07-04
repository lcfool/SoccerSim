package com.soccersim.presentation.ui.util

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

private val displayMetrics = Resources.getSystem().displayMetrics

val Int.dpToPx: Int
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics)
        .roundToInt()