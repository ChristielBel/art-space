package com.example.artspace.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Artwork(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val infoRes: Int
)