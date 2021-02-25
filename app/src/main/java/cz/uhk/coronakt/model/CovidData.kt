package cz.uhk.coronakt.model

import java.util.*

data class CovidData (val modified: Date, val data: MutableList<DayStats>)