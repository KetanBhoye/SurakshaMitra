package com.example.surakshamitra

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NearbyAgencies : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_agencies)

        val recyclerView: RecyclerView = findViewById(R.id.nearbyagencyrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dataList = getDataList()
        val adapter = AgencylistAdapter(dataList)
        recyclerView.adapter = adapter
    }

    private fun getDataList(): List<AgencyListDataModel> {

        return listOf(
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4),
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4),
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4),
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4),
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4),
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4),
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4),
            AgencyListDataModel(R.drawable.img_4, "Text 1", R.drawable.img_4, R.drawable.img_4)

            // Add more items as needed
        )
    }
}