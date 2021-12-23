package com.example.pricecompare

import android.app.PendingIntent.getActivity
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class DbHelper {

    fun getProductList(): ArrayList<Product> {
        val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
        val dbR = dbHelper.readableDatabase


        //val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME)

        val cursor = dbR.query(
            FeedReaderContract.FeedProductEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        val productList = ArrayList<Product>()
        //val catNames = mutableListOf<String>()
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

    fun loadBrandsAndSpecs(page: Int, apiKey: String, itemId: String, onDetailsLoaded: () -> Unit) {
        val queue = Volley.newRequestQueue(MyApplication.getAppContext())

        val url = "https://price-api.datayuge.com/api/v1/compare/detail?api_key=${apiKey}&id=${itemId}"
        //textView.append(itemIds[i])
        var finished = false
        var queueEl = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonObj = JSONObject(response).getJSONObject("data")
                val brand = jsonObj.getString("product_brand")
                val rating = jsonObj.getString("product_ratings")
                val storesJsonArr = jsonObj.getJSONArray("stores")////
                val dbHelper = FeedReaderDbHelper(MyApplication.getAppContext())
                val dbR = dbHelper.readableDatabase


                val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME)

                val selection = "${FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME} = ?"
                val selectionArgs = arrayOf(brand)

                //val sortOrder = "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} DESC"

                val cursor = dbR.query(
                    FeedReaderContract.FeedBrandEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
                )

                val productBrands = mutableListOf<String>()
                //val catNames = mutableListOf<String>()
                with(cursor) {
                    while (moveToNext()) {
                        val productBrand = getString(cursor.getColumnIndexOrThrow("name"))
                        //val categoryName = getString(cursor.getColumnIndexOrThrow("name"))
                        productBrands.add(productBrand)
                        //catNames.add(categoryName)
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

                //val sortOrder = "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} DESC"

                val shopCursor = dbRead.query(
                    FeedReaderContract.FeedShopsEntry.TABLE_NAME,   // The table to query
                    shopProjection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
                )

                val shopsList = ArrayList<String>()
                with(shopCursor) {
                    while (moveToNext()) {
                        val shop = getString(cursor.getColumnIndexOrThrow("name"))
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
//                for (i in 0 until storesJsonArr.length()) {
//                    val iteratorObj = storesJsonArr.getJSONObject(i).keys()
//                    while(iteratorObj.hasNext()) {
//                        val key = iteratorObj.next()
//                        val storeObj = storesJsonArr.getJSONObject(i).getJSONObject(key)
//                    }
//                    if(storesJsonArr.)
//                }


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
                val mainSpecs = mainSpecsArr.getString(0) + ", " + mainSpecsArr.getString(1) + ", " + mainSpecsArr.getString(2) + ", " + mainSpecsArr.getString(3) + ", " + mainSpecsArr.getString(4)
                val subSpecsObj = product.getJSONObject("sub_specs")
                val camera = subSpecsObj.getJSONArray("Camera")
                var rearCamera : String = ""
                var frontCamera : String = ""
                for (j in 0 until camera.length()) {
                    if (camera.getJSONObject(j).getString("spec_key") == "Rear") {
                        rearCamera = camera.getJSONObject(j).getString("spec_value")
                    } else if (camera.getJSONObject(j).getString("spec_key") == "Front") {
                        frontCamera = camera.getJSONObject(j).getString("spec_value")
                    }
                }
                val battery = subSpecsObj.getJSONArray("Battery")
                var batteryCapacity : String = ""
                for (j in 0 until battery.length()) {
                    if (battery.getJSONObject(j).getString("spec_key") == "Capacity") {
                        batteryCapacity = battery.getJSONObject(j).getString("spec_value")
                    }
                }
                val display = subSpecsObj.getJSONArray("Display")
                var displayResolution : String = ""
                var displaySize : String = ""
                for (j in 0 until display.length()) {
                    if (display.getJSONObject(j).getString("spec_key") == "Resolution") {
                        displayResolution = display.getJSONObject(j).getString("spec_value")
                    } else if (display.getJSONObject(j).getString("spec_key") == "Size") {
                        displaySize = display.getJSONObject(j).getString("spec_value")
                    }
                }

                val storage = subSpecsObj.getJSONArray("Storage")
                var internalMemory : String = ""
                var ram : String = ""
                for (j in 0 until storage.length()) {
                    if (storage.getJSONObject(j).getString("spec_key") == "Internal Memory") {
                        internalMemory = storage.getJSONObject(j).getString("spec_value")
                    } else if (storage.getJSONObject(j).getString("spec_key") == "RAM") {
                        ram = storage.getJSONObject(j).getString("spec_value")
                    }
                }
                val processor = subSpecsObj.getJSONArray("Processor")
                var chipset : String = ""
                for (j in 0 until processor.length()) {
                    if (processor.getJSONObject(j).getString("spec_key") == "Chipset") {
                        chipset = processor.getJSONObject(j).getString("spec_value")
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
                //textView.text = obj.getJSONArray("data").getJSONObject(0).getString("product_title")
//                    jsonStringOfModels.plus(response)

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
        //val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME)

        val cursor = dbR.query(
            FeedReaderContract.FeedProductEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        var productList = ArrayList<Product>()
        //val catNames = mutableListOf<String>()
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
        //val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME)

        val detailsCursor = dbR.query(
            FeedReaderContract.FeedSpecsEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            detailsSelection,              // The columns for the WHERE clause
            detailsSelectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        var detailedProductList = ArrayList<ProductHelper>()
        //val catNames = mutableListOf<String>()
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
            FeedReaderContract.FeedShopsEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        var shopList = ArrayList<String>()
        //val catNames = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val shop = getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedShopsEntry.COLUMN_SHOP_NAME))
                shopList.add(shop)
            }
        }
        cursor.close()



        val selection = "${FeedReaderContract.FeedPricesEntry.COLUMN_PRODUCT_ID} = ?"
        val selectionArgs = arrayOf(id)
        //val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME)

        val priceCursor = dbR.query(
            FeedReaderContract.FeedPricesEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        var priceList = ArrayList<Price>()
        //val catNames = mutableListOf<String>()
        with(priceCursor) {
            while (moveToNext()) {
                val shopId = getInt(priceCursor.getColumnIndexOrThrow(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_ID))
                val url = getString(priceCursor.getColumnIndexOrThrow(FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_URL))
                val price = getInt(priceCursor.getColumnIndexOrThrow(FeedReaderContract.FeedPricesEntry.COLUMN_PRICE))
                priceList.add(Price(shopList[shopId], url, price))
            }
        }
        dbR.close()
        dbHelper.close()
        return priceList
    }
}