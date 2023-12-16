package com.example.newfresh

//import com.android.volley.Response

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest


class MainActivity : AppCompatActivity(), NewsItemClicked{
    val mAdapter = RecycleViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycleViewer : RecyclerView = findViewById(R.id.recycleView)
        //attaching layoutManger to recycleView
        recycleViewer.layoutManager = LinearLayoutManager(this)

        //attaching adapter to recycleViewer
        //adapter already has viewHolder attached to it
        recycleViewer.adapter = mAdapter

        fetchNews()
    }

    private fun fetchNews(){
//        val apiUrl = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=eac8f87fa8354ba292adb889d068da7d"
        val apiUrl = "https://newsdata.io/api/1/news?apikey=pub_33123b108d56b65cd0d1e63289820d5c4f8d0&country=in&language=en&image=1&size=10"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            apiUrl,
            null,
            {
                Toast.makeText(this, "Req successfull", Toast.LENGTH_LONG).show()
                val results = it.getJSONArray("results")
                val newsArray = ArrayList<News>()
                for(i in 0 until results.length()) {
                    val newsjsonObject = results.getJSONObject(i)
                    val categoryJsonArray = newsjsonObject.getJSONArray("category")
                    val category:ArrayList<String> = ArrayList()
                    for(i in 0 until categoryJsonArray.length()){
                        category.add(categoryJsonArray.getString(i))
                    }
                    val news:News = News(
                        newsjsonObject.getString("title"),
                        newsjsonObject.getString("creator"),
                        newsjsonObject.getString("link"),
                        newsjsonObject.getString("image_url"),
                        category
                    )
                    newsArray.add(news)
                }
                mAdapter.updateItems(newsArray)
            },
            {
                Log.d("Volley Error", it.toString())
                Toast.makeText(this, "Cann't fetch the news! Please check your internet", Toast.LENGTH_LONG).show()
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun itemClickedFun(item : News){
        Toast.makeText(this, "Opening news",Toast.LENGTH_SHORT).show()
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this@MainActivity, Uri.parse(item.url))
    }
}