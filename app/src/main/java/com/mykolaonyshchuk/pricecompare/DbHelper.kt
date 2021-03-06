package com.mykolaonyshchuk.pricecompare

import android.content.ContentValues
import android.provider.BaseColumns
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class DbHelper {

    fun loadProductList(page: Int) {
        val apiKey = "VWDLvFSSpC06mSFgCxWXGJQgqfdA5CUvKKY"
        val queue = Volley.newRequestQueue(MyApplication.getAppContext())
        val url = "https://price-api.datayuge.com/api/v1/compare/search?api_key=${apiKey}&page=${page}"
        var queueEl = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonArr = JSONObject(response).getJSONArray("data")
                val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
                val dbLocal = dbHelper.writableDatabase

                val categoryValues = ContentValues().apply {
                    put(FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME, "Mobiles")
                }

                val categoryRowId = dbLocal?.insert(FeedReaderContract.FeedCategoryEntry.TABLE_NAME, null, categoryValues)
                for (i in 0 until jsonArr.length()) {
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
            },
            Response.ErrorListener { "That didn't work!" })
        queue.add(queueEl)
    }

    fun getProductList(): ArrayList<Product> {
        val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
        val dbR = dbHelper.readableDatabase


        val cursor = dbR.query(
            FeedReaderContract.FeedProductEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val productList = ArrayList<Product>()
        with(cursor) {
            while (moveToNext()) {
                val product = Product(
                    getString(cursor.getColumnIndexOrThrow("id")),
                    getString(cursor.getColumnIndexOrThrow("modelName")),
                    getInt(cursor.getColumnIndexOrThrow("lowestPrice")),
                    getString(cursor.getColumnIndexOrThrow("imageUrl")),
                    getFloat(cursor.getColumnIndexOrThrow("rating"))
                )
                productList.add(product)
            }
        }
        cursor.close()
        dbR.close()
        return productList
    }

    fun loadBrandsAndSpecs(itemId: String, onDetailsLoaded: () -> Unit) {
        val queue = Volley.newRequestQueue(MyApplication.getAppContext())

        val apiKey = "VWDLvFSSpC06mSFgCxWXGJQgqfdA5CUvKKY"
        val url = "https://price-api.datayuge.com/api/v1/compare/detail?api_key=${apiKey}&id=${itemId}"
        var finished = false
        var queueEl = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonObj = JSONObject(response).getJSONObject("data")
                val brand = jsonObj.getString("product_brand")
                val rating = jsonObj.getString("product_ratings")
                val storesJsonArr = jsonObj.getJSONArray("stores")
                val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
                val dbR = dbHelper.readableDatabase


                val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME)

                val selection = "${FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME} = ?"
                val selectionArgs = arrayOf(brand)

                val cursor = dbR.query(
                    FeedReaderContract.FeedBrandEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
                )

                val productBrands = mutableListOf<String>()
                with(cursor) {
                    while (moveToNext()) {
                        val productBrand = getString(cursor.getColumnIndexOrThrow("name"))
                        productBrands.add(productBrand)
                    }
                }
                cursor.close()
                dbR.close()

                val dbLocal = dbHelper.writableDatabase
                var found = false
                var index = 1
                for (j in 0 until productBrands.size) {
                    index = j + 1
                    if (brand == productBrands[0]) {
                        found = true
                        break
                    }
                }
                if (!found)
                {

                    val values = ContentValues().apply {
                        put(FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME, brand)
                    }
                    val newRowId = dbLocal?.insert(FeedReaderContract.FeedBrandEntry.TABLE_NAME, null, values)

                }
                val values1 = ContentValues().apply {
                    put(FeedReaderContract.FeedProductEntry.COLUMN_BRAND_ID, index)
                    put(FeedReaderContract.FeedProductEntry.COLUMN_RATING, rating)
                }

                val idSelection = "${FeedReaderContract.FeedProductEntry.COLUMN_ID} LIKE ?"
                val idSelectionArgs = arrayOf(itemId)
                val count = dbLocal.update(
                    FeedReaderContract.FeedProductEntry.TABLE_NAME,
                    values1,
                    idSelection,
                    idSelectionArgs)


                val dbRead= dbHelper.readableDatabase


                val shopProjection = arrayOf(FeedReaderContract.FeedShopsEntry.COLUMN_SHOP_NAME)

                val shopCursor = dbRead.query(
                    FeedReaderContract.FeedShopsEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )

                val shopsList = ArrayList<String>()
                with(shopCursor) {
                    while (moveToNext()) {
                        val shop = getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedShopsEntry.COLUMN_SHOP_NAME))
                        shopsList.add(shop)
                    }
                }
                shopCursor.close()
                dbRead.close()

                if (shopsList.isEmpty()) {
                    val dbWrite = dbHelper.writableDatabase
                    for (i in 0 until storesJsonArr.length()) {
                        val iteratorObj = storesJsonArr.getJSONObject(i).keys()
                        while(iteratorObj.hasNext()) {
                            val key = iteratorObj.next()
                            val values = ContentValues().apply {
                                put(FeedReaderContract.FeedShopsEntry.COLUMN_SHOP_NAME, key)
                            }
                            val newRowId = dbWrite?.insert(FeedReaderContract.FeedShopsEntry.TABLE_NAME, null, values)

                            if (storesJsonArr.getJSONObject(i).getString(key) != "[]") {
                                val storeObj = storesJsonArr.getJSONObject(i).getJSONObject(key)
                                val price = storeObj.getString("product_price")
                                val url = storeObj.getString("product_store_url")
                                val shopValues = ContentValues().apply {
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_PRODUCT_ID, itemId)
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_ID, i + 1)
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_URL, url)
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_PRICE, price)
                                }
                                val newRowId = dbWrite?.insert(FeedReaderContract.FeedPricesEntry.TABLE_NAME, null, shopValues)

                            }

                        }
                    }
                    dbWrite.close()
                } else {

                    val dbWrite = dbHelper.writableDatabase
                    for (i in 0 until storesJsonArr.length()) {
                        val iteratorObj = storesJsonArr.getJSONObject(i).keys()

                        var ind: Int = 1
                        while (iteratorObj.hasNext()) {
                            val key = iteratorObj.next()
                            for (j in 0 until shopsList.size) {
                                if (shopsList[j] == key) {
                                    ind = j + 1
                                }
                            }

                            if (storesJsonArr.getJSONObject(i).getString(key) != "[]")  {
                                val storeObj = storesJsonArr.getJSONObject(i).getJSONObject(key)
                                val price = storeObj.getString("product_price")
                                val url = storeObj.getString("product_store_url")
                                val shopValues = ContentValues().apply {
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_PRODUCT_ID, itemId)
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_ID, ind)
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_URL, url)
                                    put(FeedReaderContract.FeedPricesEntry.COLUMN_PRICE, price)
                                }
                                val newRowId = dbWrite?.insert(FeedReaderContract.FeedPricesEntry.TABLE_NAME, null, shopValues)
                            }
                        }
                    }
                    dbWrite.close()
                }


                dbLocal.close()
                dbHelper.close()
                if (finished) {
                    onDetailsLoaded()
                } else {
                    finished = true
                }
            },
            Response.ErrorListener { Toast.makeText(MyApplication.getAppContext(), "That didn't work!", Toast.LENGTH_SHORT) })
        queue.add(queueEl)

        val specsUrl = "https://price-api.datayuge.com/api/v1/compare/specs?api_key=${apiKey}&id=${itemId}"
        var specsQueueEl = StringRequest(
            Request.Method.GET, specsUrl,
            { response ->
                val product = JSONObject(response).getJSONObject("data")
                val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
                val dbLocal = dbHelper.writableDatabase


                val mainSpecsArr = product.getJSONArray("main_specs")
                var mainSpecs = " " + mainSpecsArr.getString(0)
                for (i in 1..mainSpecsArr.length() - 1) {
                    mainSpecs += ", " + mainSpecsArr.getString(i)
                }
                val subSpecsObj = product.getJSONObject("sub_specs")

                var frontCamera : String = ""
                if(subSpecsObj.has("Camera")) {
                    val camera = subSpecsObj.getJSONArray("Camera")
                    for (j in 0 until camera.length()) {
                        if (camera.getJSONObject(j).getString("spec_key") == "Front camera  (Primary)") {
                            frontCamera = camera.getJSONObject(j).getString("spec_value")
                        }
                    }
                }

                var rearCamera : String = ""
                var ram : String = ""
                if (subSpecsObj.has("Summary")) {
                    val summary = subSpecsObj.getJSONArray("Summary")
                    for (j in 0 until summary.length()) {
                        if (summary.getJSONObject(j).getString("spec_key") == "Rear Camera") {
                            rearCamera = summary.getJSONObject(j).getString("spec_value")
                        } else if (summary.getJSONObject(j).getString("spec_key") == "RAM") {
                            ram = summary.getJSONObject(j).getString("spec_value")
                        }
                    }
                }

                var batteryCapacity : String = ""
                if (subSpecsObj.has("Battery")) {
                    val battery = subSpecsObj.getJSONArray("Battery")
                    for (j in 0 until battery.length()) {
                        if (battery.getJSONObject(j).getString("spec_key") == "Capacity") {
                            batteryCapacity = battery.getJSONObject(j).getString("spec_value")
                        }
                    }
                }

                var displayResolution : String = ""
                var displaySize : String = ""
                if (subSpecsObj.has("Display")) {
                    val display = subSpecsObj.getJSONArray("Display")
                    for (j in 0 until display.length()) {
                        if (display.getJSONObject(j).getString("spec_key") == "Resolution") {
                            displayResolution = display.getJSONObject(j).getString("spec_value")
                        } else if (display.getJSONObject(j).getString("spec_key") == "Size") {
                            displaySize = display.getJSONObject(j).getString("spec_value")
                        }
                    }
                }

                var internalMemory : String = ""
                if (subSpecsObj.has("Storage")) {
                    val storage = subSpecsObj.getJSONArray("Storage")
                    for (j in 0 until storage.length()) {
                        if (storage.getJSONObject(j).getString("spec_key") == "Internal Memory") {
                            internalMemory = storage.getJSONObject(j).getString("spec_value")
                        }
                    }
                }

                var chipset : String = ""
                if(subSpecsObj.has("Performance")) {
                    val processor = subSpecsObj.getJSONArray("Performance")
                    for (j in 0 until processor.length()) {
                        if (processor.getJSONObject(j).getString("spec_key") == "Chipset") {
                            chipset = processor.getJSONObject(j).getString("spec_value")
                        }
                    }
                }
                val values = ContentValues().apply {
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_ID, itemId)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_MAIN_SPECS, mainSpecs)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_REAR_CAMERA, rearCamera)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_FRONT_CAMERA, frontCamera)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_RESOLUTION, displayResolution)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_SIZE, displaySize)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_PROCESSOR, chipset)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_INTERNAL_MEMORY, internalMemory)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_RAM, ram)
                    put(FeedReaderContract.FeedSpecsEntry.COLUMN_BATTERY, batteryCapacity)
                }


                val newRowId = dbLocal?.insert(FeedReaderContract.FeedSpecsEntry.TABLE_NAME, null, values)
                dbLocal.close()
                dbHelper.close()

                if (finished) {
                    onDetailsLoaded()
                } else {
                    finished = true
                }
            },
            Response.ErrorListener { Toast.makeText(MyApplication.getAppContext(), "That didn't work!", Toast.LENGTH_SHORT) })
        queue.add(specsQueueEl)

    }

    fun getProductDetails(id: String) : ProductHelper {
        val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
        val dbR = dbHelper.readableDatabase

        val selection = "${FeedReaderContract.FeedProductEntry.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id)

        val cursor = dbR.query(
            FeedReaderContract.FeedProductEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var productList = ArrayList<Product>()
        with(cursor) {
            while (moveToNext()) {
                val product = Product(
                    getString(cursor.getColumnIndexOrThrow("id")),
                    getString(cursor.getColumnIndexOrThrow("modelName")),
                    getInt(cursor.getColumnIndexOrThrow("lowestPrice")),
                    getString(cursor.getColumnIndexOrThrow("imageUrl")),
                    getFloat(cursor.getColumnIndexOrThrow("rating"))
                )
                productList.add(product)
            }
        }
        cursor.close()


        val detailsSelection = "${FeedReaderContract.FeedSpecsEntry.COLUMN_ID} = ?"
        val detailsSelectionArgs = arrayOf(id)

        val detailsCursor = dbR.query(
            FeedReaderContract.FeedSpecsEntry.TABLE_NAME,
            null,
            detailsSelection,
            detailsSelectionArgs,
            null,
            null,
            null
        )

        var detailedProductList = ArrayList<ProductHelper>()
        with(detailsCursor) {
            while (moveToNext()) {
                var detailedProduct = ProductHelper(
                    productList[0].id,
                    productList[0].modelName,
                    productList[0].lowestPrice,
                    productList[0].imageUrl,
                    productList[0].rating,
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_MAIN_SPECS)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_REAR_CAMERA)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_FRONT_CAMERA)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_RESOLUTION)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_SIZE)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_PROCESSOR)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_INTERNAL_MEMORY)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_RAM)),
                    getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_BATTERY))
                )
                detailedProductList.add(detailedProduct)
                getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_MAIN_SPECS))
                getString(detailsCursor.getColumnIndexOrThrow(FeedReaderContract.FeedSpecsEntry.COLUMN_REAR_CAMERA))
            }
        }
        detailsCursor.close()
        dbR.close()
        dbHelper.close()
        return detailedProductList[0]
    }

    fun getPrices(id: String) : ArrayList<Price> {
        val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
        val dbR = dbHelper.readableDatabase

        val projection = arrayOf(FeedReaderContract.FeedShopsEntry.COLUMN_SHOP_NAME)

        val cursor = dbR.query(
            FeedReaderContract.FeedShopsEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        var shopList = ArrayList<String>()
        with(cursor) {
            while (moveToNext()) {
                val shop = getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedShopsEntry.COLUMN_SHOP_NAME))
                shopList.add(shop)
            }
        }
        cursor.close()



        val selection = "${FeedReaderContract.FeedPricesEntry.COLUMN_PRODUCT_ID} = ?"
        val selectionArgs = arrayOf(id)

        val priceCursor = dbR.query(
            FeedReaderContract.FeedPricesEntry.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var priceList = ArrayList<Price>()
        with(priceCursor) {
            while (moveToNext()) {
                val shopId = getInt(priceCursor.getColumnIndexOrThrow(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_ID))
                val url = getString(priceCursor.getColumnIndexOrThrow(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_URL))
                val price = getInt(priceCursor.getColumnIndexOrThrow(FeedReaderContract.FeedPricesEntry.COLUMN_PRICE))
                priceList.add(Price(shopList[shopId - 1], url, price))
            }
        }
        dbR.close()
        dbHelper.close()
        return priceList
    }
}
