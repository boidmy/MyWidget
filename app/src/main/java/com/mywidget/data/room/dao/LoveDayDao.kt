package com.mywidget.data.room.dao

import androidx.room.*
import com.mywidget.data.room.LoveDay

@Dao
interface LoveDayDao {
    @Query("SELECT * FROM loveDay")
    fun getData(): List<LoveDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(loveDay: LoveDay)


}