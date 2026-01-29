package com.rezahosseini.taskmanager.vm

import androidx.lifecycle.ViewModel
import com.rezahosseini.taskmanager.model.network.DataImage
import com.rezahosseini.taskmanager.model.network.repository.DataImageProviderImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelDataImage :ViewModel{
    val dataImageProviderImpl:DataImageProviderImpl
    @Inject
    constructor(dataImageProviderImpl:DataImageProviderImpl){
        this.dataImageProviderImpl=dataImageProviderImpl
    }
    fun getImages(): Flow<List<DataImage>> {
        return dataImageProviderImpl.getImages()
    }
}