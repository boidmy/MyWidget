package com.mywidget.data.room.dao

import androidx.room.*
import com.mywidget.data.room.LoveDay

@Dao
abstract class LoveDayDao : BaseDao<LoveDay>{
    @Query("SELECT * FROM loveDay")
    abstract fun getData(): List<LoveDay>

}