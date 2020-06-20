package com.epita.covidata2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        val confirmed : TextView = findViewById(R.id.confirmed_country)
        val death : TextView = findViewById(R.id.deaths_country)
        val recovered : TextView = findViewById(R.id.recovered_country)
        val listcountry : RecyclerView = findViewById(R.id.listcountry)

        val retrofit: Retrofit =  Retrofit.Builder().baseUrl("https://api.covid19api.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val service : GlobalInfoHolder = retrofit.create(GlobalInfoHolder::class.java)

        val call : Callback<SummaryGlobal> = object : Callback<SummaryGlobal>{
            override fun onFailure(call: Call<SummaryGlobal>, t: Throwable) {
                confirmed.text = "Failed"
            }

            override fun onResponse(call: Call<SummaryGlobal>, response: Response<SummaryGlobal>) {
                if (response.code() == 200)
                {
                    if (response.body() != null) {
                        val data: SummaryGlobal = response.body()!!
                        confirmed.text = data.Global.TotalConfirmed.toString()
                        death.text = data.Global.TotalDeaths.toString()
                        recovered.text = data.Global.TotalRecovered.toString()

                        val countryList : MutableList<Country> = arrayListOf()
                        data.Countries.forEach { c -> countryList.add(c) }
                        val onItemClickListener = View.OnClickListener{clickedRowView ->
                            val clickedcountry : Country = countryList[clickedRowView.tag as Int]
                            val intent = Intent(this@DataActivity, CountryActivity::class.java)
                            intent.putExtra("nameCountry", clickedcountry.Country)
                            startActivity(intent)
                            //Toast.makeText(this@DataActivity, "clicked : " + clickedcountry.Country, Toast.LENGTH_SHORT).show()
                        }
                        listcountry.adapter = CountryAdapter(this@DataActivity, countryList, onItemClickListener)
                        listcountry.setHasFixedSize(true)
                        listcountry.layoutManager = LinearLayoutManager(this@DataActivity)
                        listcountry.addItemDecoration(DividerItemDecoration(this@DataActivity, LinearLayoutManager.VERTICAL))
                    }
                }
            }
        }
        service.getinfo().enqueue(call)
    }
}