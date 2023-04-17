package com.mymealapp.ui.categories

import com.mymealapp.data.model.Category
import com.mymealapp.ui.base.ErrorState

sealed class CategoriesState{
    object Loading : CategoriesState()
    data class Success(val categories: List<Category>) : CategoriesState()
    data class Failure(val error: ErrorState? = null) : CategoriesState()
}
