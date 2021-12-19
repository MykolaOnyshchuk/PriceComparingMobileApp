package com.example.pricecompare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        var productId: String? = getIntent().getStringExtra("productObj")

    }
}