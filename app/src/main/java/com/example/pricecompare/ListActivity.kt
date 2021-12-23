package com.example.pricecompare

import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
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

        findViewById<AppCompatImageButton>(R.id.toolbar_menu).setOnClickListener {
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            }else{
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        val dbHelperObj = DbHelper()
        val productList = dbHelperObj.getProductList()

        findViewById<EditText>(R.id.toolbar_search).addTextChangedListener {
            val searchText = it.toString()

            val list = ArrayList<Product>()
            for(item in productList){
                if(item.modelName.contains(searchText)){
                    list.add(item)
                }
            }

            val adapter = ProductAdapter(this, list)
            listView.adapter = adapter
        }

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






        //val listItems = arrayOfNulls<String>(products.size)
//        for (i in 0 until recipeList.size) {
//            val recipe = recipeList[i]
//            listItems[i] = recipe.title
//        }

        val adapter = ProductAdapter(this, productList)
        listView.adapter = adapter

        val context = MyApplication.getAppContext()
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedProduct = productList[position]

            //val detailIntent = ProductDetailsActivity.newIntent(context, selectedRecipe)

            val apiKey = "VWDLvFSSpC06mSFgCxWXGJQgqfdA5CUvKKY"
            var dbHelper = DbHelper()
            //dbHelper.loadBrandsAndSpecs(1, apiKey, selectedProduct.id)
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