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
    fun getDataById(id: Long): DataEntity?

    @Query("SELECT * FROM data WHERE done = :done")
    fun getDataByDone(done: Int): Flow<List<DataEntity>>

    @Query("SELECT * FROM data WHERE name = :name")
    fun getDataByName(name: String): Flow<List<DataEntity>>

    @Query("SELECT * FROM data WHERE about = :about")
    fun getDataByAbout(about: String): Flow<List<DataEntity>>

    @Query("SELECT * FROM data WHERE priority = :priority")
    fun getDataByPriority(priority:Int): Flow<List<DataEntity>>

    @Query("""
SELECT * FROM data
WHERE name LIKE '%' || :query || '%'
ORDER BY 
CASE WHEN :sort = 'AZ' THEN name END COLLATE NOCASE ASC,
CASE WHEN :sort = 'ZA' THEN name END COLLATE NOCASE DESC,
CASE WHEN :sort = 'PRIO_HIGH' THEN priority END DESC,
CASE WHEN :sort = 'PRIO_LOW' THEN priority END ASC,
CASE WHEN :sort = 'DATE_NEW' THEN id END DESC,
CASE WHEN :sort = 'DATE_OLD' THEN id END ASC
""")
    fun getFiltered(
        query: String,
        sort: String
    ): Flow<List<DataEntity>>


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

    @Query("DELETE FROM data")
    suspend fun deleteAll()

    @Query("DELETE FROM data WHERE done = :done")
    suspend fun deleteByDone(done: Int)
}
