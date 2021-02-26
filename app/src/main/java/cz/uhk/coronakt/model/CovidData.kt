package cz.uhk.coronakt.model

import java.util.*

/**
 * Data class drzici odpoved z WS - modified se zatim nikde nepouziva
 */
data class CovidData (val modified: Date, val data: MutableList<DayStats>)