package io.github.catomon.yutaka.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PostEntity)

    @Update
    suspend fun update(item: PostEntity)

    @Delete
    suspend fun delete(item: PostEntity)

    @Query("SELECT * FROM posts")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts")
    suspend fun getListOfAll(): List<PostEntity>

    @Query("DELETE FROM posts")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PostEntity>)
}