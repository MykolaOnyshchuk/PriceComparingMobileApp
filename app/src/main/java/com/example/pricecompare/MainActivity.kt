package com.example.pricecompare

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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

        val textView = findViewById<TextView>(R.id.textView1)
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
        //setJsonString("crd")



//        fun loadBrandsAndSpecs(page: Int, apiKey: String) {
//            val queue = Volley.newRequestQueue(MyApplication.getAppContext())
//            val dbHelper = FeedReaderDbHelper(this)
//            val dbR = dbHelper.readableDatabase
//
//
//            val projection = arrayOf(FeedReaderContract.FeedProductEntry.COLUMN_ID)
//
//            //val selection = "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} = ?"
//            //val selectionArgs = arrayOf("Cars")
//
//            //val sortOrder = "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} DESC"
//
//            val cursor = dbR.query(
//                FeedReaderContract.FeedProductEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                null,              // The columns for the WHERE clause
//                null,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                null               // The sort order
//            )
//
//            val itemIds = mutableListOf<String>()
//            //val catNames = mutableListOf<String>()
//            with(cursor) {
//                while (moveToNext()) {
//                    val itemId = getString(cursor.getColumnIndexOrThrow("id"))
//                    textView.append(itemId)
//                    //val categoryName = getString(cursor.getColumnIndexOrThrow("modelName"))
//                    itemIds.add(itemId)
//                    //catNames.add(categoryName)
//                }
//            }
//            cursor.close()
//            dbR.close()
//            dbHelper.close()
//
//            if (itemIds.isEmpty()) {
//                //textView.text = "Empty"
//            }
//            for (i in 0 until itemIds.size) {
//                val url = "https://price-api.datayuge.com/api/v1/compare/detail?api_key=${apiKey}&id=${itemIds[i]}"
//                textView.append(itemIds[i])
//                var queueEl = StringRequest(
//                    Request.Method.GET, url,
//                    { response ->
//                        val jsonObj = JSONObject(response).getJSONObject("data")
//                        val brand = jsonObj.getString("product_brand")
//                        val rating = jsonObj.getString("product_ratings")
//                        val dbHelper = FeedReaderDbHelper(this)
//                        val dbR = dbHelper.readableDatabase
//
//
//                        val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME)
//
//                        val selection = "${FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME} = ?"
//                        val selectionArgs = arrayOf(brand)
//
//                        //val sortOrder = "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} DESC"
//
//                        val cursor = dbR.query(
//                            FeedReaderContract.FeedBrandEntry.TABLE_NAME,   // The table to query
//                            projection,             // The array of columns to return (pass null to get all)
//                            selection,              // The columns for the WHERE clause
//                            selectionArgs,          // The values for the WHERE clause
//                            null,                   // don't group the rows
//                            null,                   // don't filter by row groups
//                            null               // The sort order
//                        )
//
//                        val productBrands = mutableListOf<String>()
//                        //val catNames = mutableListOf<String>()
//                        with(cursor) {
//                            while (moveToNext()) {
//                                val productBrand = getString(cursor.getColumnIndexOrThrow("name"))
//                                //val categoryName = getString(cursor.getColumnIndexOrThrow("name"))
//                                productBrands.add(productBrand)
//                                //catNames.add(categoryName)
//                            }
//                        }
//                        cursor.close()
//                        dbR.close()
//
//                        val dbLocal = dbHelper.writableDatabase
//                        var found = false
//                        var index = 1
//                        for (j in 0 until productBrands.size) {
//                            index = j + 1
//                            if (brand == productBrands[0]) {
//                                found = true
//                                break
//                            }
//                        }
//                        if (!found)
//                        {
//
//                            val values = ContentValues().apply {
//                                put(FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME, brand)
//                            }
//                            val newRowId = dbLocal?.insert(FeedReaderContract.FeedBrandEntry.TABLE_NAME, null, values)
//
//                        }
//                        val values1 = ContentValues().apply {
//                            put(FeedReaderContract.FeedProductEntry.COLUMN_BRAND_ID, index)
//                            put(FeedReaderContract.FeedProductEntry.COLUMN_RATING, rating)
//                        }
//
//                        val idSelection = "${FeedReaderContract.FeedProductEntry.COLUMN_ID} LIKE ?"
//                        val idSelectionArgs = arrayOf(itemIds[i])
//                        val count = dbLocal.update(
//                            FeedReaderContract.FeedProductEntry.TABLE_NAME,
//                            values1,
//                            idSelection,
//                            idSelectionArgs)
//
//                        dbLocal.close()
//                        dbHelper.close()
//                    },
//                    Response.ErrorListener { jsonString = "That didn't work!" })
//                queue.add(queueEl)
//
//                val specsUrl = "https://price-api.datayuge.com/api/v1/compare/specs?api_key=${apiKey}&id=${itemIds[i]}"
//                var specsQueueEl = StringRequest(
//                    Request.Method.GET, specsUrl,
//                    { response ->
//                        val product = JSONObject(response).getJSONObject("data")
//                        val dbHelper = FeedReaderDbHelper(this)
//                        val dbLocal = dbHelper.writableDatabase
//
//
//                        val mainSpecsArr = product.getJSONArray("main_specs")
//                        val mainSpecs = mainSpecsArr.getString(0) + ", " + mainSpecsArr.getString(1) + ", " + mainSpecsArr.getString(2) + ", " + mainSpecsArr.getString(3) + ", " + mainSpecsArr.getString(4)
//                        val subSpecsObj = product.getJSONObject("sub_specs")
//                        val camera = subSpecsObj.getJSONArray("Camera")
//                        var rearCamera : String = ""
//                        var frontCamera : String = ""
//                        for (j in 0 until camera.length()) {
//                            if (camera.getJSONObject(j).getString("spec_key") == "Rear") {
//                                rearCamera = camera.getJSONObject(j).getString("spec_value")
//                            } else if (camera.getJSONObject(j).getString("spec_key") == "Front") {
//                                frontCamera = camera.getJSONObject(j).getString("spec_value")
//                            }
//                        }
//                        val battery = subSpecsObj.getJSONArray("Battery")
//                        var batteryCapacity : String = ""
//                        for (j in 0 until battery.length()) {
//                            if (battery.getJSONObject(j).getString("spec_key") == "Capacity") {
//                                batteryCapacity = battery.getJSONObject(j).getString("spec_value")
//                            }
//                        }
//                        val display = subSpecsObj.getJSONArray("Display")
//                        var displayResolution : String = ""
//                        var displaySize : String = ""
//                        for (j in 0 until display.length()) {
//                            if (display.getJSONObject(j).getString("spec_key") == "Resolution") {
//                                displayResolution = display.getJSONObject(j).getString("spec_value")
//                            } else if (display.getJSONObject(j).getString("spec_key") == "Size") {
//                                displaySize = display.getJSONObject(j).getString("spec_value")
//                            }
//                        }
//
//                        val storage = subSpecsObj.getJSONArray("Storage")
//                        var internalMemory : String = ""
//                        var ram : String = ""
//                        for (j in 0 until storage.length()) {
//                            if (storage.getJSONObject(j).getString("spec_key") == "Internal Memory") {
//                                internalMemory = storage.getJSONObject(j).getString("spec_value")
//                            } else if (storage.getJSONObject(j).getString("spec_key") == "RAM") {
//                                ram = storage.getJSONObject(j).getString("spec_value")
//                            }
//                        }
//                        val processor = subSpecsObj.getJSONArray("Processor")
//                        var chipset : String = ""
//                        for (j in 0 until processor.length()) {
//                            if (processor.getJSONObject(j).getString("spec_key") == "Chipset") {
//                                chipset = processor.getJSONObject(j).getString("spec_value")
//                            }
//                        }
//                        val values = ContentValues().apply {
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_ID, itemIds[i])
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_MAIN_SPECS, mainSpecs)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_REAR_CAMERA, rearCamera)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_FRONT_CAMERA, frontCamera)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_RESOLUTION, displayResolution)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_SIZE, displaySize)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_PROCESSOR, chipset)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_INTERNAL_MEMORY, internalMemory)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_RAM, ram)
//                            put(FeedReaderContract.FeedSpecsEntry.COLUMN_BATTERY, batteryCapacity)
//                        }
//
//                        val newRowId = dbLocal?.insert(FeedReaderContract.FeedSpecsEntry.TABLE_NAME, null, values)
//                        dbLocal.close()
//                        dbHelper.close()
//                        //textView.text = obj.getJSONArray("data").getJSONObject(0).getString("product_title")
////                    jsonStringOfModels.plus(response)
//                    },
//                    Response.ErrorListener { jsonString = "That didn't work!" })
//                queue.add(specsQueueEl)
//            }
//
//        }




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
                        textView.append(newRowId.toString())
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

        //textView.text = JSONObject(jsonString).getJSONArray("data").getJSONObject(0).getString("product_title")

        //var jsonStringOfModels: String = ""

//
//        val url = "https://price-api.datayuge.com/api/v1/compare/search?api_key=${apiKey}&page=1"
//        var queueEl = StringRequest(
//            Request.Method.GET, url,
//            { response ->
//                // Display the first 500 characters of the response string.
//                val obj = JSONObject(response)
//                textView.text = obj.getJSONArray("data").getJSONObject(0).getString("product_title")
//                jsonStringOfModels.plus(response)
//            },
//            { jsonStringOfModels = "That didn't work!" })
//
//
//
//        // Add the request to the RequestQueue.
//        queue.add(queueEl)

        //textView.text = jsonStringOfModels
        //Log.d("MyActivityyyyyyyyyy", jsonStringOfModels)

//        jsonStringOfModels = "[" + jsonStringOfModels[0]
//        jsonStringOfModels = jsonStringOfModels[0] + "]"
//        val gson = GsonBuilder().create()
//
//        val theList = gson.fromJson<ArrayList<String>>(jsonStringOfModels, object :
//            TypeToken<ArrayList<String>>(){}.type)
//
//
//
//        for (model in theList) {
//            val parser: Parser = Parser()
//            val json: JsonObject = parser.parse(model) as JsonObject
//            //println("Name : ${json.string("name")}, Age : ${json.int("age")}")
//
//            //Тут маємо викликати метод, що одразу записуватиме все до бд
//            if(json.string("product_title") == "Realme 8i") {
//                textView.text = "Realme 8i"
//            }
//        }

//        val dbHelper = FeedReaderDbHelper(this)
//        val db = dbHelper.writableDatabase
//
//        // Create a new map of values, where column names are the keys
//        val values = ContentValues().apply {
//            //put(FeedReaderContract.FeedEntry.COLUMN_ID, 1)
//            put(FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME, "Cars")
//        }
//
//// Insert the new row, returning the primary key value of the new row
//        val newRowId = db?.insert(FeedReaderContract.FeedCategoryEntry.TABLE_NAME, null, values)
//
//        val values2 = ContentValues().apply {
//            //put(FeedReaderContract.FeedEntry.COLUMN_ID, 1)
//            put(FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME, "Mobiles")
//        }
//
//// Insert the new row, returning the primary key value of the new row
//        val newRowId2 = db?.insert(FeedReaderContract.FeedCategoryEntry.TABLE_NAME, null, values2)
//
//        val values3 = ContentValues().apply {
//            //put(FeedReaderContract.FeedEntry.COLUMN_ID, 1)
//            put(FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME, "Laptops")
//        }
//
//// Insert the new row, returning the primary key value of the new row
//        val newRowId3 = db?.insert(FeedReaderContract.FeedCategoryEntry.TABLE_NAME, null, values3)
//
//        db.close()
//
///////////////////////////
//
//        val dbR = dbHelper.readableDatabase
//
//
//        val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME)
//
//        val selection = "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} = ?"
//        val selectionArgs = arrayOf("Cars")
//
//        val sortOrder = "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} DESC"
//
//        val cursor = dbR.query(
//            FeedReaderContract.FeedCategoryEntry.TABLE_NAME,   // The table to query
//            projection,             // The array of columns to return (pass null to get all)
//            selection,              // The columns for the WHERE clause
//            selectionArgs,          // The values for the WHERE clause
//            null,                   // don't group the rows
//            null,                   // don't filter by row groups
//            sortOrder               // The sort order
//        )
//
//        val itemIds = mutableListOf<Long>()
//        val catNames = mutableListOf<String>()
//        with(cursor) {
//            while (moveToNext()) {
//                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
//                val categoryName = getString(cursor.getColumnIndexOrThrow("name"))
//                itemIds.add(itemId)
//                catNames.add(categoryName)
//            }
//        }
//        cursor.close()
//        dbR.close()
//        //textView.text = catNames[2]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}