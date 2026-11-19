
package com.rezahosseini.taskmanager.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.repository.DataImageProvider
import com.rezahosseini.taskmanager.vm.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViewModelDataImage @Inject constructor(
    private val dataImageProvider: DataImageProvider
) : ViewModel() {

    val getImages: StateFlow<UiState<List<DataImage>>> =
        dataImageProvider.getImages()
            .map<List<DataImage>, UiState<List<DataImage>>> {
                UiState.Success(it)
            }
            .onStart {
                emit(UiState.Loading)
            }
            .catch { error ->
                emit(
                    UiState.Error(
                        error.message ?: "Unknown error"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )
}
