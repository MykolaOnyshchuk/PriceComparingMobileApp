package com.example.pricecompare

import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ListActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        listView = findViewById<ListView>(R.id.recipe_list_view)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

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



        val dbHelperObj = DbHelper()
        val productList = dbHelperObj.getProductList()

        productList[0].modelName = "Infinix V15"
        productList[0].lowestPrice = 9699
        productList[1].modelName = "Infinix Smart 5"
        productList[1].lowestPrice = 7199
        productList[2].modelName = "Infinix 10s"
        productList[2].lowestPrice = 8499
        productList[3].modelName = "Realme C21Y"
        productList[3].lowestPrice = 8999
        productList[4].modelName = "Xiaomi Redmi 9i"
        productList[4].lowestPrice = 8499
        productList[5].modelName = "Xiaomi Poco C31"
        productList[5].lowestPrice = 8499
        productList[6].modelName = "Infinix Hot 10 Play"
        productList[6].lowestPrice = 9099

//
//        productList[6].modelName = "Apple Iphone 13 Pro"
//        productList[6].lowestPrice = 169990
//        productList[1].modelName = "Apple Iphone 12 Pro"
//        productList[1].lowestPrice = 139000
//        productList[4].modelName = "Apple Iphone 12 Pro Max"
//        productList[4].lowestPrice = 159900
//        productList[2].modelName = "Samsung Galaxy Z Fold3"
//        productList[2].lowestPrice = 157999
//        productList[3].modelName = "Samsung Galaxy Z Fold2"
//        productList[3].lowestPrice = 157999
//        productList[5].modelName = "Apple Iphone XS"
//        productList[5].lowestPrice = 134900
//        productList[0].modelName = "Samsung Galaxy S9 Plus"
//        productList[0].lowestPrice = 117990

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}