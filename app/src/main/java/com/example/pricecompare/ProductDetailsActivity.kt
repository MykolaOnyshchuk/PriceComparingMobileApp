package com.example.pricecompare

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.util.Linkify
import android.view.Gravity
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import java.util.concurrent.TimeUnit


class ProductDetailsActivity : AppCompatActivity() {

    lateinit var productName: TextView
    lateinit var productPriceText: TextView
    lateinit var productRatingText: TextView
    lateinit var mainSpecifications: TextView
    lateinit var rearCameraSpecs: TextView
    lateinit var frontCameraSpecs: TextView
    lateinit var screenResolutionSpecs: TextView
    lateinit var screenSizeSpecs: TextView
    lateinit var processorSpecs: TextView
    lateinit var internalMemorySpecs: TextView
    lateinit var ramSpecs: TextView
    lateinit var batterySpecs: TextView

    lateinit var priceComparingList: List<Price>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val specificationsLayout = findViewById<LinearLayout>(R.id.specifications_layout)
        val priceComparingLayout = findViewById<LinearLayout>(R.id.price_comparing_layout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 0){
                    specificationsLayout.isVisible = true
                    priceComparingLayout.isVisible = false
                }else{
                    specificationsLayout.isVisible = false
                    priceComparingLayout.isVisible = true
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        findViewById<RadioGroup>(R.id.radio_group).setOnCheckedChangeListener {
                _, i ->
            if(i==R.id.radio_a_z){
                buildComparingList(0)
            }else if(i==R.id.radio_z_a){
                buildComparingList(1)
            }else if(i==R.id.radio_high_low){
                buildComparingList(2)
            }else if(i==R.id.radio_low_high){
                buildComparingList(3)
            }
        }
        productName = findViewById(R.id.productName)
        productPriceText = findViewById(R.id.productPriceText)
        productRatingText = findViewById(R.id.productRatingText)
        mainSpecifications = findViewById(R.id.mainSpecifications)
        rearCameraSpecs = findViewById(R.id.rearCameraSpecs)
        frontCameraSpecs = findViewById(R.id.frontCameraSpecs)
        screenResolutionSpecs = findViewById(R.id.screenResolutionSpecs)
        screenSizeSpecs = findViewById(R.id.screenSizeSpecs)
        processorSpecs = findViewById(R.id.processorSpecs)
        internalMemorySpecs = findViewById(R.id.internalMemorySpecs)
        ramSpecs = findViewById(R.id.ramSpecs)
        batterySpecs = findViewById(R.id.batterySpecs)

        var productId: String = getIntent().getStringExtra("productObj").toString()

        var dbHelperObj = DbHelper()
        val apiKey = "VWDLvFSSpC06mSFgCxWXGJQgqfdA5CUvKKY"


        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lastSync = sharedPreferences.getLong(productId, -1)

        val priceList = dbHelperObj.getPrices(productId)

        var dbHelper = FeedReaderDbHelper(this)
        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSync) > 60 && lastSync != -1L && !priceList.isEmpty()) {
            val dbLocal = dbHelper.writableDatabase
            dbLocal.delete(FeedReaderContract.FeedPricesEntry.TABLE_NAME, FeedReaderContract.FeedPricesEntry.COLUMN_PRODUCT_ID + "='" + productId + "'", null)
            dbLocal.delete(FeedReaderContract.FeedSpecsEntry.TABLE_NAME, FeedReaderContract.FeedSpecsEntry.COLUMN_ID + "='" + productId + "'", null)
        }

        val dbR = dbHelper.readableDatabase
        val idSelection = "${FeedReaderContract.FeedSpecsEntry.COLUMN_ID} LIKE ?"
        val idSelectionArgs = arrayOf(productId)
        val cursor = dbR.query(
            FeedReaderContract.FeedSpecsEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val idList = ArrayList<String>()
        with(cursor) {
            while (moveToNext()) {
                idList.add(getString(cursor.getColumnIndexOrThrow("id")))
            }
        }
        cursor.close()
        dbR.close()

        if (idList.isEmpty() || lastSync == -1L || TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSync) > 60) {
            val dbH = DbHelper()
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putLong(productId, System.currentTimeMillis())
            editor.apply()
            dbH.loadBrandsAndSpecs(productId) {
                callback(dbHelperObj, productId)
            }
        } else {
            callback(dbHelperObj, productId)
        }
    }

    fun callback (dbHelperObj: DbHelper, productId: String) {
        var detailedProduct = dbHelperObj.getProductDetails(productId)

        productName.text = detailedProduct.modelName
        productPriceText.text = detailedProduct.lowestPrice.toString()
        productRatingText.text = detailedProduct.rating.toString()
        mainSpecifications.append(detailedProduct.mainSpecs)
        rearCameraSpecs.text = detailedProduct.rearCamera
        frontCameraSpecs.text = detailedProduct.frontCamera
        screenResolutionSpecs.text = detailedProduct.ScreenResolution
        screenSizeSpecs.text = detailedProduct.ScreenSize
        processorSpecs.text = detailedProduct.processor
        internalMemorySpecs.text = detailedProduct.internalMemory
        ramSpecs.text = detailedProduct.ram
        batterySpecs.text = detailedProduct.battery


        priceComparingList = dbHelperObj.getPrices(productId)
        buildComparingList(0)
    }

    private fun buildComparingList(sortType: Int){
        findViewById<LinearLayout>(R.id.price_list).removeAllViews()

        when (sortType) {
            0 -> {
                priceComparingList = priceComparingList.sortedBy { it.shop }
            }
            1 -> {
                priceComparingList = priceComparingList.sortedByDescending { it.shop }
            }
            2 -> {
                priceComparingList = priceComparingList.sortedByDescending { it.price }
            }
            else -> {
                priceComparingList = priceComparingList.sortedBy { it.price }
            }
        }

        for(item in priceComparingList){
            val tableRow = TableRow(this)

            val shop = TextView(this)
            shop.layoutParams = TableRow.LayoutParams(pxFromDp(96), TableRow.LayoutParams.MATCH_PARENT)
            shop.text = item.shop
            shop.setTextColor(Color.BLACK)
            shop.setTypeface(null, Typeface.BOLD)
            shop.gravity = Gravity.CENTER
            tableRow.addView(shop)

            val url = TextView(this)
            val urlParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            url.text = item.url
            url.setTextColor(Color.DKGRAY)
            urlParams.weight = 1f
            url.layoutParams = urlParams
            url.autoLinkMask = Linkify.WEB_URLS
            url.maxLines = 2
            url.setPadding(0, 65, 0, 65)
            Linkify.addLinks(url, Linkify.WEB_URLS)
            url.linksClickable = true

            tableRow.addView(url)

            val price = TextView(this)
            price.layoutParams = TableRow.LayoutParams(pxFromDp(96), TableRow.LayoutParams.MATCH_PARENT)
            price.text = item.price.toString()
            price.setTextColor(Color.BLACK)
            price.setTypeface(null, Typeface.BOLD)
            price.gravity = Gravity.CENTER
            tableRow.addView(price)

            findViewById<LinearLayout>(R.id.price_list).addView(tableRow)
        }
    }

    private fun pxFromDp(dp: Int): Int {
        return dp * applicationContext.resources.displayMetrics.density.toInt()
    }
}