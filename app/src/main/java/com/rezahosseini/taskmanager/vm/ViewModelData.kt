package com.rezahosseini.taskmanager.vm

import androidx.lifecycle.ViewModel
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class ViewModelData(
    private var dataRepository:DataRepository) : ViewModel(){
    fun getAllData(): Flow<List<DataEntity>> {
        return dataRepository!!.getAllData()
    }
    suspend fun getDataById(id: Long): DataEntity?{
        return dataRepository!!.getDataById(id)
    }
    fun getDataByDone(done: Int): Flow<List<DataEntity>> {
        return dataRepository!!.getDataByDone(done)
    }
    suspend fun getDataByName(name: String): DataEntity?{
        return dataRepository!!.getDataByName(name)
    }
    fun getDataByAbout(about: String): Flow<List<DataEntity>> {
        return dataRepository!!.getDataByAbout(about)
    }
}