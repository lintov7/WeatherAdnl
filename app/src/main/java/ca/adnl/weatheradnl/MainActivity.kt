package ca.adnl.weatheradnl

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ca.adnl.weatheradnl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dbHelper: DBHelper
    lateinit var adapter:CityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Cities"
        adapter = CityAdapter(arrayListOf(), object: CityAdapter.CityCallback{
            override fun onCityClicked(pos: Int) {
                val intent = Intent(this@MainActivity, WeatherActivity::class.java)
                intent.putExtra(AddCityActivity.CITY_PAYLOAD, adapter.items[pos])
                startActivity(intent)
            }
            override fun onEditClicked(pos: Int) {
                val intent = Intent(this@MainActivity, AddCityActivity::class.java)
                intent.putExtra(AddCityActivity.CITY_PAYLOAD, adapter.items[pos])
                startActivity(intent)
            }
        })
        dbHelper = DBHelper(this)
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddCityActivity::class.java)
            startActivity(intent)
        }

        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val cities = dbHelper.getAllCities()
        adapter.setNewItems(cities)
    }
}