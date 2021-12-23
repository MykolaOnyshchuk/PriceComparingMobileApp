package com.example.pricecompare

import android.content.Context
import android.graphics.Color
import android.icu.number.FractionPrecision
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import org.w3c.dom.Text


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
        dbHelperObj.loadBrandsAndSpecs(1, apiKey, productId) {
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

            for(item in dbHelperObj.getPrices(productId)){
                val tableRow = TableRow(this)

                val shop = TextView(this)
                shop.layoutParams = TableRow.LayoutParams(pxFromDp(96), TableRow.LayoutParams.MATCH_PARENT)
                shop.text = item.shop
                shop.setTextColor(Color.BLACK)
                shop.gravity = Gravity.CENTER
                tableRow.addView(shop)

                val url = TextView(this)
                val urlParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
                url.text = item.url
                url.setTextColor(Color.DKGRAY)
                urlParams.weight = 1f
                url.layoutParams = urlParams

                tableRow.addView(url)

                val price = TextView(this)
                price.layoutParams = TableRow.LayoutParams(pxFromDp(96), TableRow.LayoutParams.MATCH_PARENT)
                price.text = item.price.toString()
                price.setTextColor(Color.BLACK)
                price.gravity = Gravity.CENTER
                tableRow.addView(price)

                findViewById<LinearLayout>(R.id.price_comparing_layout).addView(tableRow)

//                Toast.makeText(applicationContext, item.toString(), Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun pxFromDp(dp: Int): Int {
        return dp * applicationContext.resources.displayMetrics.density.toInt()
    }
}