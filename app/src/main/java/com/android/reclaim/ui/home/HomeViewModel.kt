package com.android.reclaim.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.reclaim.data.model.CopingStrategy
import com.android.reclaim.ui.strategy.StrategyViewModel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var recommendedToday by mutableStateOf<List<CopingStrategy>>(emptyList())

    fun loadRecommendedStrategies(userId: String?, strategyVM: StrategyViewModel) {
        if (userId == null) return
        viewModelScope.launch {
            val topTrigger = strategyVM.linkedStrategies.values.flatten().firstOrNull() ?: "Stress"
            val timeBased = strategyVM.recommendedForTrigger(topTrigger)
            val smart = strategyVM.recommendedSmart(userId, topTrigger)

            val combined = (timeBased + smart).distinctBy { it.id }.take(5)
            recommendedToday = combined
        }
    }
}
