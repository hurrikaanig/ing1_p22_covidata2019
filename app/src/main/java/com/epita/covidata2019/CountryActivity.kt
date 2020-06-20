package com.epita.covidata2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_data)

        val country : TextView = findViewById(R.id.country_name_id)
        val confirmed : TextView = findViewById(R.id.confirmed_country)
        val death : TextView = findViewById(R.id.deaths_country)
        val recovered : TextView = findViewById(R.id.recovered_country)
        val calendar : CalendarView = findViewById(R.id.calendarView)
        val target_country : String = intent.getStringExtra("nameCountry")
        country.text = target_country
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var the_day : String = "-" + dayOfMonth.toString() + "T00:00:00Z"
            if (dayOfMonth < 10)
                the_day = "-0"+ dayOfMonth.toString() + "T00:00:00Z"
            var the_month : String = "-" + (month + 1).toString()
            if (month + 1 < 10)
                the_month = "-0" + (month + 1).toString()
            var the_date : String = year.toString()
            the_date += the_month + the_day

            val retrofit: Retrofit =  Retrofit.Builder().baseUrl("https://api.covid19api.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val service : GlobalInfoHolder = retrofit.create(GlobalInfoHolder::class.java)

            val call : Callback<List<CasesByCountry>> = object : Callback<List<CasesByCountry>>{
                override fun onFailure(call: Call<List<CasesByCountry>>, t: Throwable) {
                    confirmed.text = "Failed"
                }

                override fun onResponse(
                    call: Call<List<CasesByCountry>>,
                    response: Response<List<CasesByCountry>>
                ) {
                    if (response.code() == 200)
                    {
                        if (response.body() != null)
                        {
                            val data : List<CasesByCountry> = response.body()!!
                            var goodDate : Boolean = false
                            for (info in data)
                            {
                                if (the_date == info.Date)
                                {
                                    goodDate = true
                                    confirmed.text = info.Confirmed
                                    death.text = info.Deaths
                                    recovered.text = info.Recovered
                                }
                            }
                            if (goodDate == false)
                            {
                                confirmed.text = "Not found"
                                death.text = "Not found"
                                recovered.text = "Not found"
                            }
                        }
                    }
                }
            }
            service.getcountryname(target_country).enqueue(call)
        }
    }
}