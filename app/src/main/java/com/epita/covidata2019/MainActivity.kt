package com.epita.covidata2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonGoData.setOnClickListener{
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
        }

        buttonGoGraph.setOnClickListener{
            val intent = Intent(this, GraphActivity::class.java)
            startActivity(intent)
        }



    }


}