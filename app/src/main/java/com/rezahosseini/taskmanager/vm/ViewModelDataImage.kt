package com.rezahosseini.taskmanager.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.repository.DataImageProvider
import com.rezahosseini.taskmanager.model.network.repository.DataImageProviderImpl
import com.rezahosseini.taskmanager.vm.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewModelDataImage @Inject constructor(
    val dataImageProvider:DataImageProvider):ViewModel(){

    val getImages: StateFlow<UiState<List<DataImage>>> =
        kotlinx.coroutines.flow.flow {

            emitAllImages()
        }
            .catch { error ->
                emit(
                    UiState.Error(
                        error.message ?: "Unknown error"
                    )
                )
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                UiState.Loading
            )


    private suspend fun kotlinx.coroutines.flow.FlowCollector<UiState<List<DataImage>>>.emitAllImages() {
        emit(UiState.Loading)

        dataImageProvider.getImages()
            .map<List<DataImage>, UiState<List<DataImage>>> {
                UiState.Success(it)
            }
            .collect { emit(it) }
    }
}