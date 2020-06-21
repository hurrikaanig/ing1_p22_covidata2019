package com.epita.covidata2019

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_mystery_evolution.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EvoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mystery_evolution)

        val Countryname : TextView = findViewById(R.id.Countrynamemystery)
        val GoodScore : ImageView = findViewById(R.id.goodimage)
        val BadScore : ImageView = findViewById(R.id.badimage)
        var theCountry : String = ""
        var date1 : String = ""
        var confirmed1 : String = ""
        var death1 : String = ""
        var recovered1 : String = ""
        var date2 : String = ""
        var confirmed2 : String = ""
        var death2 : String = ""
        var recovered2 : String = ""

        val buttonConfirmed : Button = findViewById(R.id.confiButton)
        val buttonDeath : Button = findViewById(R.id.deaButton)
        val buttonRecovered : Button = findViewById(R.id.recovButton)
        var disp : Int = 1

        val listcountry : RecyclerView = findViewById(R.id.countrylistEvo)
        val listDate1 : RecyclerView = findViewById(R.id.dateone)
        val listDate2 : RecyclerView = findViewById(R.id.datetwo)


        val retrofit: Retrofit =  Retrofit.Builder().baseUrl("https://api.covid19api.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val service : GlobalInfoHolder = retrofit.create(GlobalInfoHolder::class.java)
        val service2 : GlobalInfoHolder = retrofit.create(GlobalInfoHolder::class.java)
        val service3 : GlobalInfoHolder = retrofit.create(GlobalInfoHolder::class.java)


        val call3 : Callback<List<CasesByCountry>> = object : Callback<List<CasesByCountry>>
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

                        val onItemClickListener = View.OnClickListener{ clickedRowView ->
                            val clickedDate : CasesByCountry = infoList[clickedRowView.tag as Int]
                            confirmed2 = clickedDate.Confirmed
                            death2 = clickedDate.Deaths
                            recovered2 = clickedDate.Recovered
                            date2 = clickedDate.Date
                            buttonConfirmed.visibility = View.VISIBLE
                            buttonDeath.visibility = View.VISIBLE
                            buttonRecovered.visibility = View.VISIBLE
                            Toast.makeText(this@EvoActivity, "Choose the type of cases", Toast.LENGTH_LONG).show()
                        }
                        listDate2.adapter = DateAdapter(this@EvoActivity, infoList, onItemClickListener)
                        listDate2.setHasFixedSize(true)
                        listDate2.layoutManager = LinearLayoutManager(this@EvoActivity)
                    }
                }
            }
        }


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

                        val onItemClickListener = View.OnClickListener{ clickedRowView ->
                            val clickedDate : CasesByCountry = infoList[clickedRowView.tag as Int]
                            confirmed1 = clickedDate.Confirmed
                            death1 = clickedDate.Deaths
                            recovered1 = clickedDate.Recovered
                            date1 = clickedDate.Date
                            Toast.makeText(this@EvoActivity, "Choose the second date", Toast.LENGTH_LONG).show()

                            service3.getcountryname(theCountry).enqueue(call3)
                        }
                        listDate1.adapter = DateAdapter(this@EvoActivity, infoList, onItemClickListener)
                        listDate1.setHasFixedSize(true)
                        listDate1.layoutManager = LinearLayoutManager(this@EvoActivity)
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
                        data.Countries.forEach { c -> countryList.add(c) }
                        val onItemClickListener = View.OnClickListener{ clickedRowView ->
                            val clickedcountry : Country = countryList[clickedRowView.tag as Int]
                            theCountry = clickedcountry.Country
                            Countryname.text = theCountry
                            Toast.makeText(this@EvoActivity, "Choose the first date", Toast.LENGTH_LONG).show()
                            service2.getcountryname(theCountry).enqueue(call2)

                        }
                        listcountry.adapter = CountryAdapter(this@EvoActivity, countryList, onItemClickListener)
                        listcountry.setHasFixedSize(true)
                        listcountry.layoutManager = LinearLayoutManager(this@EvoActivity)
                        listcountry.addItemDecoration(DividerItemDecoration(this@EvoActivity, LinearLayoutManager.VERTICAL))
                    }
                }
            }
        }


        buttonConfirmed.setOnClickListener {
                infoview1.text = "Confirmed : " + confirmed1
                infoview2.text = "Confirmed : " + confirmed2
                progressfstdate.max = 100000
                progresssndate.max = 100000
                ObjectAnimator.ofInt(progressfstdate, "progress", confirmed1.toInt())
                    .setDuration(1500).start()
                ObjectAnimator.ofInt(progresssndate, "progress", confirmed2.toInt())
                    .setDuration(1500).start()
            if (confirmed1.toInt() > confirmed2.toInt())
            {
                goodimage.visibility = View.VISIBLE
                badimage.visibility = View.INVISIBLE
            }
            else
            {
                goodimage.visibility = View.INVISIBLE
                badimage.visibility = View.VISIBLE
            }
        }

        buttonDeath.setOnClickListener {
                infoview1.text = "Death : " + death1
                infoview2.text = "Death : " + death2
                progressfstdate.max = 20000
                progresssndate.max = 20000
                ObjectAnimator.ofInt(progressfstdate, "progress", death1.toInt()).setDuration(1500)
                    .start()
                ObjectAnimator.ofInt(progresssndate, "progress", death2.toInt()).setDuration(1500)
                    .start()
            if (death1.toInt() > death2.toInt())
            {
                goodimage.visibility = View.VISIBLE
                badimage.visibility = View.INVISIBLE
            }
            else
            {
                goodimage.visibility = View.INVISIBLE
                badimage.visibility = View.VISIBLE
            }
        }
        buttonRecovered.setOnClickListener {
                infoview1.text = "Recovered : " + recovered1
                infoview2.text = "Recovered : " + recovered2
                progressfstdate.max = 50000
                progresssndate.max = 50000
                ObjectAnimator.ofInt(progressfstdate, "progress", recovered1.toInt())
                    .setDuration(1500).start()
                ObjectAnimator.ofInt(progresssndate, "progress", recovered2.toInt())
                    .setDuration(1500).start()
            if (recovered1.toInt() < recovered2.toInt())
            {
                goodimage.visibility = View.VISIBLE
                badimage.visibility = View.INVISIBLE
            }
            else
            {
                goodimage.visibility = View.INVISIBLE
                badimage.visibility = View.VISIBLE
            }
        }
        Toast.makeText(this, "Choose a country", Toast.LENGTH_LONG).show()
        service.getinfo().enqueue(call)

    }
}