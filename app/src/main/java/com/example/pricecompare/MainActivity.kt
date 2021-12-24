package com.example.pricecompare

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var button: Button
    var jsonString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        findViewById<AppCompatImageButton>(R.id.toolbar_menu).setOnClickListener {
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            }else{
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }


        button = findViewById(R.id.button)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)


        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_phones_and_smartphones -> Toast.makeText(applicationContext, "Clicked Phones and smartphones", Toast.LENGTH_SHORT).show()
                R.id.nav_mobiles -> {
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                }
//                R.id.nav_sync -> Toast.makeText(applicationContext, "Clicked Sync", Toast.LENGTH_SHORT).show()
//                R.id.nav_trash -> Toast.makeText(applicationContext, "Clicked Trash", Toast.LENGTH_SHORT).show()
//                R.id.nav_setting -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
//                R.id.nav_login -> Toast.makeText(applicationContext, "Clicked Log In", Toast.LENGTH_SHORT).show()
//                R.id.nav_share -> Toast.makeText(applicationContext, "Clicked Share", Toast.LENGTH_SHORT).show()
//                R.id.nav_rate_us -> Toast.makeText(applicationContext, "Clicked Rate Us", Toast.LENGTH_SHORT).show()
            }

            true

        }
        // ...

        // Instantiate the RequestQueue.
        val apiKey = "VWDLvFSSpC06mSFgCxWXGJQgqfdA5CUvKKY"
        //val url = "https://price-api.datayuge.com/api/v1/compare/search?api_key=VWDLvFSSpC06mSFgCxWXGJQgqfdA5CUvKKY&page=1"

        // Request a string response from the provided URL.
        /*val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
                textView.text = "Response is: ${response}"
            },
            { textView.text = "That didn't work!" })*/


//        fun createGetReq (apiKey: String, page: Int): String {
//            val url = "https://price-api.datayuge.com/api/v1/compare/search?api_key=${apiKey}&page=${page}"
//            var returnStat = ""
//            StringRequest(
//                Request.Method.GET, url,
//                { response ->
//                    // Display the first 500 characters of the response string.
//                    returnStat = response
//                },
//                { returnStat = "That didn't work!" })
//            return returnStat
//        }

        fun setJsonString(value: String) {
            this.jsonString = value
        }




        fun loadProductList(page: Int, apiKey: String) {
            val queue = Volley.newRequestQueue(MyApplication.getAppContext())
            val url = "https://price-api.datayuge.com/api/v1/compare/search?api_key=${apiKey}&page=${page}"
            var queueEl = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val jsonArr = JSONObject(response).getJSONArray("data")
                    val dbHelper = FeedReaderDbHelper(this)
                    val dbLocal = dbHelper.writableDatabase

                    val categoryValues = ContentValues().apply {
                        put(FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME, "Mobiles")
                    }

                    dbLocal.delete(FeedReaderContract.FeedProductEntry.TABLE_NAME, null, null)

                    val categoryRowId = dbLocal?.insert(FeedReaderContract.FeedCategoryEntry.TABLE_NAME, null, categoryValues)
                    for (i in 0 until jsonArr.length()) {//////////////
                        val product = jsonArr.getJSONObject(i)

                        val values = ContentValues().apply {
                            put(FeedReaderContract.FeedProductEntry.COLUMN_ID, product.getString("product_id"))
                            put(FeedReaderContract.FeedProductEntry.COLUMN_MODEL_NAME, product.getString("product_title"))
                            put(FeedReaderContract.FeedProductEntry.COLUMN_LOWEST_PRICE, product.getString("product_lowest_price"))
                            put(FeedReaderContract.FeedProductEntry.COLUMN_IMAGE_URL, product.getString("product_image"))
                            put(FeedReaderContract.FeedProductEntry.COLUMN_CATEGORY_ID, 1)
                        }

                        val newRowId = dbLocal?.insert(FeedReaderContract.FeedProductEntry.TABLE_NAME, null, values)
                    }
                    dbLocal.close()
                    dbHelper.close()

                    //loadBrandsAndSpecs(1, apiKey)

                    //textView.text = obj.getJSONArray("data").getJSONObject(0).getString("product_title")
//                    jsonStringOfModels.plus(response)
                },
                Response.ErrorListener { jsonString = "That didn't work!" })



            // Add the request to the RequestQueue.
            queue.add(queueEl)
        }

        loadProductList(1, apiKey)

        button.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            // start your next activity
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}