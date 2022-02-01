package com.transport.mall.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.transport.mall.model.ChatMessagesListModel

@Dao
interface ChatDao {

    @Insert(onConflict = REPLACE)
    fun insert(article: ChatMessagesListModel)

    @Query("Select * from ChatMessagesListModel Where _id=:userId AND domain=:domain")
    fun getChatList(userId: String, domain: String): List<ChatMessagesListModel>

    @Query("Select * from ChatMessagesListModel Where _id=:userId AND mobileTimeStamp=:mobileTimeStamp AND domain=:domain")
    fun getChat(userId: String, mobileTimeStamp: Long, domain: String): ChatMessagesListModel

    @Delete
    fun delete(chat: ChatMessagesListModel)

    /*   @Insert(onConflict = REPLACE)
       fun insert(articles: List<ChatMessagesListModel>)

       @Delete
       fun delete(article: ChatMessagesListModel)

       @Delete
       fun deletelist(articlelist: List<ChatMessagesListModel>)

       @Update
       fun update(article: ChatMessagesListModel)

       @Update
       fun update(articles: List<ChatMessagesListModel>)
   */

}

