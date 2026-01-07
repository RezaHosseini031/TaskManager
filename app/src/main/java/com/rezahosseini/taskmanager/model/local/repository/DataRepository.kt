package com.rezahosseini.taskmanager.model.local.repository

import android.content.Context
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.dao.DataDao
import com.rezahosseini.taskmanager.model.local.database.AppDatabase
import kotlinx.coroutines.flow.Flow


class DataRepository {
    private var daoData: DataDao? = null
    constructor(context: Context){
        daoData= AppDatabase.getInstance(context).dataDao()
    }
    fun getAllData(): Flow<List<DataEntity>>{
        return daoData!!.getAllData()
    }
    suspend fun getDataById(id: Long): DataEntity?{
        return daoData!!.getDataById(id)
    }
    fun getDataByDone(done: Int): Flow<List<DataEntity>>{
        return daoData!!.getDataByDone(done)
    }
    suspend fun getDataByName(name: String): DataEntity?{
        return daoData!!.getDataByName(name)
    }
    fun getDataByAbout(about: String): Flow<List<DataEntity>>{
        return daoData!!.getDataByAbout(about)
    }
    suspend fun insertData(data: DataEntity){

    }
    suspend fun updateData(data: DataEntity){

    }
    suspend fun deleteData(data: DataEntity){

    }
    suspend fun deleteDataById(id: Long){

    }
}