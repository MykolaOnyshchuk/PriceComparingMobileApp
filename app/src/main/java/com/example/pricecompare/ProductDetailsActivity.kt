package com.example.pricecompare

import android.icu.number.FractionPrecision
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

//interface MyCallback{
//    fun onValueChanged()
//}
//
//interface MyInterface{
//    fun onCallback(response:Boolean)
//}

class ProductDetailsActivity : AppCompatActivity() {
    val myInterface = this

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
        }

    }
}