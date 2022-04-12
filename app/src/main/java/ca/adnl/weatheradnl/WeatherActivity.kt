package ca.adnl.weatheradnl

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import ca.adnl.weatheradnl.databinding.ActivityWeatherBinding
import ca.adnl.weatheradnl.models.City
import ca.adnl.weatheradnl.models.WeatherData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.*

class WeatherActivity : AppCompatActivity() {
    var scope = CoroutineScope(Dispatchers.IO)
    lateinit var binding: ActivityWeatherBinding
    var city: City? = null
    companion object{
        const val IconBaseUrl = "https://openweathermap.org/img/wn/"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Weather Details"
        setContentView(binding.root)
        city = intent.getSerializableExtra(AddCityActivity.CITY_PAYLOAD) as City?
        if(city != null){
            scope.launch {
                Repository().getWeatherData(cityName = city!!.name, object : ResultCallback{
                    override fun onSuccess(data: String) {
                        runOnUiThread {
                            binding.flLoading.visibility = View.VISIBLE
                            println(data)
                            val weatherData = Gson().fromJson(data, WeatherData::class.java)
                            binding.apply {
                                tvCityName.text = weatherData.name
                                tvLat.text = "Lat: ${weatherData.coord.lat}"
                                tvLong.text = "Long: ${weatherData.coord.lon}"

                                if(weatherData.weather.isNotEmpty()){
                                    val weather = weatherData.weather[0]
                                    Glide.with(this@WeatherActivity).load("$IconBaseUrl${weather.icon}@2x.png").placeholder(R.drawable.ic_placeholder_image)
                                        .dontAnimate()
                                        .into(ivWeatherIcon)
                                    tvWeatherName.text = weather.main
                                }
                                binding.tvTemp.text = SpannableStringBuilder()
                                    .bold { append("Temperature: ") }
                                    .append("${weatherData.main.temp} ℃")
                                binding.tvFeelsLike.text = SpannableStringBuilder()
                                    .bold { append("Feels Like: ") }
                                    .append("${weatherData.main.temp} ℃")
                                binding.tvMin.text = SpannableStringBuilder()
                                    .bold { append("Temperature min: ") }
                                    .append("${weatherData.main.tempMin} ℃")
                                binding.tvMax.text = SpannableStringBuilder()
                                    .bold { append("Temperature max: ") }
                                    .append("${weatherData.main.tempMax} ℃")
                                binding.flLoading.visibility = View.GONE

                            }
                        }
                    }

                    override fun onError(error: String) {
                        runOnUiThread {
                            Toast.makeText(this@WeatherActivity, error, Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                    }
                })
            }
        }else{
            Toast.makeText(this, "Error city object not found", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}