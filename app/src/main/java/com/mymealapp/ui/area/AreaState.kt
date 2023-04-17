package com.mymealapp.ui.area

import com.mymealapp.data.model.Area
import com.mymealapp.data.model.Meal
import com.mymealapp.ui.base.ErrorState

sealed class AreaState {
    object Loading : AreaState()
    data class SuccessAreaList(val areas: List<Area>) : AreaState()
    data class SuccessByArea(val meal: List<Meal>) : AreaState()
    data class Failure(val error: ErrorState? = null) : AreaState()
}
