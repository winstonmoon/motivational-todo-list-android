package com.moonwinston.motivationaltodolist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class DmlState: Parcelable {
    @Parcelize
    data class Insert(val method: String): DmlState()
    @Parcelize
    object Update: DmlState()
    @Parcelize
    object Delete: DmlState()
}
