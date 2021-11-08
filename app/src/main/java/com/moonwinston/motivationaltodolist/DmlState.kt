package com.moonwinston.motivationaltodolist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class DmlState: Parcelable {
    @Parcelize
    object Insert: DmlState()
    @Parcelize
    object Update: DmlState()
    @Parcelize
    object Delete: DmlState()
}
