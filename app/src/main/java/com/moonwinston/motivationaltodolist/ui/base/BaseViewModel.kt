package com.moonwinston.motivationaltodolist.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    private var stateBundle: Bundle? = null

    open fun storeState(stateBundle: Bundle) {
        this.stateBundle = stateBundle

    }

}