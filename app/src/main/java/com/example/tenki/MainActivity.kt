package com.example.tenki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tenki.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var resultText = ""
    private var placeLat = 35.689499
    private var placeLon = 139.691711

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.weatherButton.setOnClickListener {
            // 天気と時刻を取得
            getWeatherNews()
            // 結果をtextViewに表示
            binding.textView.text = resultText

        }
        binding.weatherButton10.setOnClickListener {
            // 天気と時刻を取得
            getWeatherNews10()
            // 結果をtextViewに表示
            binding.textView.text = resultText

        }
    }

    // unixtimeからフォーマットの日付に変換
    private fun unixTimeChange(unixTime: String): String {
        var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
        var nowTime = Date(unixTime.toInt() * 1000L)
        return sdf.format(nowTime)
    }


    private fun getWeatherNews10(): Job = GlobalScope.launch {
        // 結果を初期化
        resultText = ""
        // APIを使う際に必要なKEY
        var API_KEY = "d289d0bf6812738f68ce0f8ca4d70671"
        // URL。場所と言語・API_KEYを添付
        var API_URL = "https://api.openweathermap.org/data/2.5/onecall?" +
                "lat=" + placeLat + "&" +
                "lon=" + placeLon + "&" +
                "lang=" + "ja" + "&" +
                "APPID=" + API_KEY
        var url = URL(API_URL)
        //APIから情報を取得する.
        var br = BufferedReader(InputStreamReader(url.openStream()))
        // 所得した情報を文字列化
        var str = br.readText()
        //json形式のデータとして識別
        var json = JSONObject(str)
        // hourlyの配列を取得
        var hourly = json.getJSONArray("hourly")

            for (i in 5..9) {
                var firstObject = hourly.getJSONObject(i)
                var weatherList = firstObject.getJSONArray("weather").getJSONObject(0)
                // unixtime形式で保持されている時刻を取得
                var time = firstObject.getString("dt")
                // 天気を取得
                var descriptionText = weatherList.getString("description")
                resultText += "${unixTimeChange(time)}  $descriptionText \n\n"
            }


    }


    private fun getWeatherNews(): Job = GlobalScope.launch {
        // 結果を初期化
        resultText = ""
        //APIキー
        val API_KEY = "d289d0bf6812738f68ce0f8ca4d70671"
        //都市のID(東京)
        val CITY_ID = 1850147
        //アクセスする際のURL
        val API_URL = "http://api.openweathermap.org/data/2.5/forecast?" +
                "id=" + CITY_ID + "&" +
                "lang=" + "ja" + "&" +
                "APPID=" + API_KEY
        var url = URL(API_URL)

        //APIから情報を取得する.
        var br = BufferedReader(InputStreamReader(url.openStream()))
        // 所得した情報を文字列化
        var str = br.readText()
        //json形式のデータとして識別
        var json = JSONObject(str)

        var list = json.getJSONArray("list")
        for (i in 3..10) {
            var firstObject = list.getJSONObject(i)
            var weatherList = firstObject.getJSONArray("weather").getJSONObject(0)
            // unixtime形式で保持されている時刻を取得
            var time = firstObject.getString("dt")
            // 天気を取得
            var descriptionText = weatherList.getString("description")
            resultText += "${unixTimeChange(time)}  $descriptionText \n\n"
        }
    }


}