package cz.uhk.coronakt.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "daystats")
data class DayStats(@SerializedName("datum")
                    @PrimaryKey
                    val day:Date,
                    @SerializedName("pocet_PCR_testy")
                    val pcrCnt: Int,
                    @SerializedName("pocet_AG_testy")
                    val ag : Int,
                    @SerializedName("incidence_pozitivni")
                    val positive: Int)