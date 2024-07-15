package com.example.stocksapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteDataKeys>)

    @Query("Select * From remote_key Where stock_id = :id AND stockCategory= :stockCategory")
    suspend fun getRemoteKeyByAuthorID(stockCategory: String,id: String): RemoteDataKeys?

//    @Query("Select * From remote_key Where content = :id")
//    suspend fun getRemoteKeyByContent(id: String): RemoteDataKeys?

    @Query("Delete From remote_key where stockCategory=:stockCategory")
    suspend fun clearRemoteKeys(stockCategory: String)

    @Query("Select created_at From remote_key where stockCategory=:stockCategory Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(stockCategory: String): Long?
}