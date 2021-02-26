package cz.uhk.coronakt.db

import androidx.room.*
import cz.uhk.coronakt.model.DayStats

/**
 * Dao pro pristup dennim statistikam
 * @see DayStats
 */
@Dao
interface DayStatsDao {
    /**
     * Vraci  seznam statistik serazeny sestupne podle data
     */
    @Query("SELECT * FROM daystats ORDER BY day desc")
    fun getAll() : List<DayStats>

    /**
     * Vlozeni statistik do DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg dayStats : DayStats)

}