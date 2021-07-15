package com.example.squareoffkotlin

import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    private var modelList: MutableList<Model>? = null
    private var recyclerView: RecyclerView? = null
    var mainHandler = Handler()
    var progressDialog: ProgressDialog? = null
    private var adapter: Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.main_recycleView)
        initalizedDataList() // method to call the inner class from the main class

        //oriantation check and measures
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "potrate", Toast.LENGTH_SHORT).show()
            val manager = LinearLayoutManager(this@MainActivity)
            manager.orientation = RecyclerView.VERTICAL
            recyclerView?.layoutManager = manager
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            val staggeredGridLayoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            recyclerView?.layoutManager = staggeredGridLayoutManager
        }
        fletchData().start()
    }

    private fun initalizedDataList() {
        modelList = ArrayList<Model>()
        adapter = Adapter(modelList as ArrayList<Model>)
        recyclerView!!.adapter = adapter
    }

    // class to fletch data from the api
    internal inner class fletchData : Thread() {
        var data = ""
        override fun run() {
            super.run()
            mainHandler.post {
                progressDialog = ProgressDialog(this@MainActivity)
                progressDialog!!.setMessage("Fetching Data")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()
            }

            //code connect and get data from the api
            try {
                val url = URL("https://followchess.com/config.json")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?= null
                while (bufferedReader.readLine().also { line = it } != null) {
                    data = data + line
                }
                if (!data.isEmpty()) {
                    val jsonObject = JSONObject(data)
                    val trns = jsonObject.getJSONArray("trns")
                    modelList!!.clear()
                    for (i in 0 until trns.length()) {
                        val move = trns.getJSONObject(i)
                        val name = move.getString("name")
                        val slug = move.getString("slug")
                        val status = move.getLong("status")
                        try {
                            val img = move.getString("img")
                            modelList!!.add(Model(name, slug, img, status))
                        } catch (e: Exception) {
                            modelList!!.add(Model(name, slug, "", status))
                        }
                    }
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            mainHandler.post {
                if (progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}