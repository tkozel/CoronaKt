package cz.uhk.coronakt

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import cz.uhk.coronakt.db.CovidDatabase
import cz.uhk.coronakt.model.DayStats
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class ChartActivity : AppCompatActivity() {
    lateinit var chart: LineChart
    var data: MutableList<DayStats?>? = null

    @SuppressLint("SimpleDateFormat")
    var dateFormat = SimpleDateFormat("EE d.M")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        chart = findViewById(R.id.chart)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initChart()
    }

    private fun initChart() {
        Executors.newSingleThreadExecutor().execute {
            data = CovidDatabase.getInstance(this)
                .dayStatsDao()
                .getAll().reversed()
                .toMutableList()
            val entries: MutableList<Entry> = ArrayList()
            val i = 0
            data?.forEach { ds ->
                entries.add(
                    Entry(
                        (ds!!.day.time / 3600000 / 24 + 1).toFloat(),
                        ds.positive.toFloat()
                    )
                )
            }
            val dataSet = LineDataSet(entries, "Positive")
            dataSet.color = Color.RED
            val lineData = LineData(dataSet)
            chart.data = lineData
            val xAxis = chart.xAxis!!
            with (xAxis) {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return dateFormat.format(Date((value * 3600000 * 24).toLong()))
                    }
                }
                isEnabled = true
                position = XAxis.XAxisPosition.BOTH_SIDED
            }
            with(chart) {
                description.text = "COVID-19"
                //isAutoScaleMinMaxEnabled = true
                setPinchZoom(false)
                isDragEnabled = true
                isDoubleTapToZoomEnabled = true
                //setVisibleXRangeMaximum(30f)
                invalidate()
            }
        }
    }
}