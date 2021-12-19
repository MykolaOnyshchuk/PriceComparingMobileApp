package com.example.pricecompare

import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class ListActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        listView = findViewById<ListView>(R.id.recipe_list_view)
        val dbHelperObj = DbHelper()
        val productList = dbHelperObj.getProductList()
        //val listItems = arrayOfNulls<String>(products.size)
//        for (i in 0 until recipeList.size) {
//            val recipe = recipeList[i]
//            listItems[i] = recipe.title
//        }

        val adapter = ProductAdapter(this, productList)
        listView.adapter = adapter

        val context = this
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = productList[position]

            //val detailIntent = ProductDetailsActivity.newIntent(context, selectedRecipe)
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productObj", selectedProduct.id)
            startActivity(intent)
        }

//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
//        listView.adapter = adapter
    }
}