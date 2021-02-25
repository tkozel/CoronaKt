package cz.uhk.coronakt.db

import androidx.room.*
import cz.uhk.coronakt.model.DayStats

@Dao
interface DayStatsDao {
    @Query("SELECT * FROM daystats ORDER BY day desc")
    fun getAll() : List<DayStats>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg dayStats : DayStats)

}