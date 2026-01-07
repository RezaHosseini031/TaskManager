package com.rezahosseini.taskmanager.model.local.dao

import androidx.room.*
import com.rezahosseini.taskmanager.model.local.DataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DataDao {

    /* ---------- SELECT ---------- */

    @Query("SELECT * FROM data")
    fun getAllData(): Flow<List<DataEntity>>

    @Query("SELECT * FROM data WHERE id = :id")
    suspend fun getDataById(id: Long): DataEntity?

    @Query("SELECT * FROM data WHERE done = :done")
    fun getDataByDone(done: Int): Flow<List<DataEntity>>

    @Query("SELECT * FROM data WHERE name = :name")
    suspend fun getDataByName(name: String): DataEntity?

    @Query("SELECT * FROM data WHERE about = :about")
    fun getDataByAbout(about: String): Flow<List<DataEntity>>

    /* ---------- INSERT ---------- */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: DataEntity)

    /* ---------- UPDATE ---------- */

    @Update
    suspend fun updateData(data: DataEntity)

    /* ---------- DELETE ---------- */

    @Delete
    suspend fun deleteData(data: DataEntity)

    @Query("DELETE FROM data WHERE id = :id")
    suspend fun deleteDataById(id: Long)
}
