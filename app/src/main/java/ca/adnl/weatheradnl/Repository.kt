package ca.adnl.weatheradnl

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface ResultCallback {
    fun onSuccess(data: String)
    fun onError(error: String)
}

class Repository {
    fun getWeatherData(cityName:String, callback:ResultCallback) {
        val url = URL("https://api.openweathermap.org/data/2.5/weather?q=$cityName&units=metric&appid=2c8d07a872803f8287189b1366f2dda6")
        (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "application/json; utf-8")
            setRequestProperty("Accept", "application/json")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            try {
                val inputStream = httpURLConnection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var inputLine: String?
                val response = StringBuffer()
                while (bufferedReader.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                bufferedReader.close()
                callback.onSuccess(response.toString())
                return
            } catch (exp:Exception){
                println(exp)
                callback.onError(exp.toString())
                return
            }
        }
        callback.onError("Cannot open HttpURLConnection")
    }
}
