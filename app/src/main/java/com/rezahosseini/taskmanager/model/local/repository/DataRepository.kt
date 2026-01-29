package com.rezahosseini.taskmanager.model.local.repository

import android.content.Context
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.model.local.dao.DataDao
import com.rezahosseini.taskmanager.model.local.database.AppDatabase
import kotlinx.coroutines.flow.Flow


class DataRepository(context: Context) {

    private val daoData: DataDao = AppDatabase.getInstance(context).dataDao()

    fun getAllData(): Flow<List<DataEntity>> = daoData.getAllData()
    fun getFiltered( query: String, sort: String): Flow<List<DataEntity>> = daoData.getFiltered(query, sort)

    suspend fun insertData(data: DataEntity) = daoData.insertData(data)
    suspend fun updateData(data: DataEntity) = daoData.updateData(data)
    suspend fun deleteData(data: DataEntity) = daoData.deleteData(data)
    suspend fun deleteDataById(id: Long) = daoData.deleteDataById(id)
    suspend fun deleteAll() = daoData.deleteAll()
    suspend fun deleteByDone(done: Int) = daoData.deleteByDone(done)
}

