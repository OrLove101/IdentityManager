package com.OrLove.peoplemanager.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UnidirectionalViewModel<STATE, EVENT, EFFECT> {
    val state: StateFlow<STATE>
    val effect: Flow<EFFECT>

    fun updateUiState(newUiState: STATE)

    fun updateUiState(block: STATE.() -> STATE)

    fun event(event: EVENT)

    fun CoroutineScope.emitSideEffect(effect: EFFECT)
}