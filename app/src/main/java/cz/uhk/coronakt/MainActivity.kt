package cz.uhk.coronakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import cz.uhk.coronakt.ui.CovidDataAdapter
import cz.uhk.coronakt.ui.StatsViewModel
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private val model : StatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recView = findViewById<RecyclerView>(R.id.rvItems)
        model.data.observe(this) {
            recView.adapter = CovidDataAdapter(it)
        }
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun downloadData(menuItem: MenuItem?) {
        val ex = Executors.newSingleThreadExecutor()
        ex.execute {
            model.downloadData()
        }
    }

    private fun loadData() {
        val ex = Executors.newSingleThreadExecutor()
        ex.execute {
            model.loadDbData()
        }
    }
}