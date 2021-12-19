package com.example.pricecompare

import android.app.PendingIntent.getActivity
import android.content.Context
import android.provider.BaseColumns

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
}