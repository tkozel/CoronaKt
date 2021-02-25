package cz.uhk.coronakt.db

import android.content.Context
import androidx.room.*
import cz.uhk.coronakt.model.DayStats
import java.sql.Timestamp
import java.util.*

@Database(entities = [DayStats::class], version = 1, exportSchema = false)
@TypeConverters(CovidDatabase.MyConverter::class)
abstract class CovidDatabase : RoomDatabase() {
    class MyConverter {
        @TypeConverter
        fun fromDate(datum : Date?) : Long? {
            return datum?.time
        }

        @TypeConverter
        fun toDate(timestamp : Long?) : Date? {
            return timestamp?.let { Date(timestamp) }
        }
    }

    abstract fun dayStatsDao(): DayStatsDao

    companion object {
        @Volatile
        private var INSTANCE : CovidDatabase? = null

        fun getInstance(context : Context) : CovidDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                        CovidDatabase::class.java,
                        "covid-db")
                        .fallbackToDestructiveMigration()
                        .fallbackToDestructiveMigrationOnDowngrade()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}