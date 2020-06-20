package com.epita.covidata2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_graph_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_data)

        /*
        var confirmed : Boolean = false
        var death : Boolean = false
        var recovered : Boolean = false

        ConfirmedButton.setOnClickListener{
            confirmed = true
            death = false
            recovered = false
        }
        DeathButton.setOnClickListener{
            confirmed = false
            death = true
            recovered = false
        }
        RecoveredButton.setOnClickListener{
            confirmed = false
            death = false
            recovered = true
        }
         */

        val listcountry : RecyclerView = findViewById(R.id.horizontalList)
        val listdata : RecyclerView = findViewById(R.id.ListOfData)

        val retrofit: Retrofit =  Retrofit.Builder().baseUrl("https://api.covid19api.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val service : GlobalInfoHolder = retrofit.create(GlobalInfoHolder::class.java)
        val service2 : GlobalInfoHolder = retrofit.create(GlobalInfoHolder::class.java)

        val call2 : Callback<List<CasesByCountry>> = object : Callback<List<CasesByCountry>>
        {
            override fun onFailure(call: Call<List<CasesByCountry>>, t: Throwable) {

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
                        val infoList : MutableList<CasesByCountry> = arrayListOf()
                        data.forEach { c -> infoList.add(c) }
                        listdata.adapter = GraphAdapter(this@GraphActivity, infoList)
                        listdata.setHasFixedSize(true)
                        listdata.layoutManager = LinearLayoutManager(this@GraphActivity)
                        listdata.addItemDecoration(DividerItemDecoration(this@GraphActivity, LinearLayoutManager.VERTICAL))
                    }
                }
            }
        }

        val call : Callback<SummaryGlobal> = object : Callback<SummaryGlobal> {
            override fun onFailure(call: Call<SummaryGlobal>, t: Throwable) {
            }

            override fun onResponse(call: Call<SummaryGlobal>, response: Response<SummaryGlobal>) {
                if (response.code() == 200)
                {
                    if (response.body() != null) {
                        val data: SummaryGlobal = response.body()!!

                        val countryList : MutableList<Country> = arrayListOf()
                        var thecountry : Country
                        data.Countries.forEach { c -> countryList.add(c) }
                        val onItemClickListener = View.OnClickListener{ clickedRowView ->
                            val clickedcountry : Country = countryList[clickedRowView.tag as Int]
                            //val intent = Intent(this@GraphActivity, CountryActivity::class.java)
                            //intent.putExtra("nameCountry", clickedcountry.Country)
                            //startActivity(intent)
                            Toast.makeText(this@GraphActivity, "clicked : " + clickedcountry.Country, Toast.LENGTH_SHORT).show()
                            service2.getcountryname(clickedcountry.Country).enqueue(call2)
                        }
                        listcountry.adapter = CountryAdapter(this@GraphActivity, countryList, onItemClickListener)
                        listcountry.setHasFixedSize(true)
                        listcountry.layoutManager = LinearLayoutManager(this@GraphActivity, LinearLayoutManager.HORIZONTAL, false)
                        listcountry.addItemDecoration(DividerItemDecoration(this@GraphActivity, LinearLayoutManager.HORIZONTAL))
                    }
                }
            }
        }
        service.getinfo().enqueue(call)
    }
}