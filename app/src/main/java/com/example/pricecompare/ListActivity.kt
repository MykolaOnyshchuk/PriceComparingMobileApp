package com.example.pricecompare

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat.getActionView
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlin.math.max


class ListActivity : AppCompatActivity() {
    var sort = false
    val filterA = false
    var filterB = false
    var search = false
    var sortV = 0
    val filterAV = 0
    var filterBV = 0
    var searchV = ""
    val dbHelperObj = DbHelper()
    val productList = dbHelperObj.getProductList()

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

//        val container = findViewById<View>(R.id.sortingLayout) as ConstraintLayout
//
//        val view = inflate(this, R.layout.activity_list, container)


//        View.inflate(applicationContext, R.layout.activity_list, false)
//            .findViewById<Button>(R.id.button2)
//            .setOnClickListener {
//                Toast.makeText(applicationContext, "asd", Toast.LENGTH_SHORT).show()
//            }

        var sortedA_Z = true
        var minimum = 0
        var maximum = 9999999

        findViewById<NavigationView>(R.id.nav_view)
            .menu[2]
            .subMenu[0]
            .actionView
            .findViewById<RadioGroup>(R.id.rGroup).setOnCheckedChangeListener {
                    _, i ->
                sortedA_Z = (i == R.id.radioButton)
            }

        val filterLayout = findViewById<NavigationView>(R.id.nav_view)
            .menu[3]
            .subMenu[0]
            .actionView


        filterLayout.findViewById<Button>(R.id.button2).setOnClickListener {
            //minimum
            val minimumString =
                filterLayout.findViewById<EditText>(R.id.plain_text_input).text.toString()
            minimum = if(minimumString.isEmpty())
                0
            else
                minimumString.toInt()
            // maximum
            val maximumString =
                filterLayout.findViewById<EditText>(R.id.plain_text_input2).text.toString()

            maximum = if(maximumString.isEmpty())
                9999999
            else
                maximumString.toInt()

            //filter
            var list = productList.filter { it.lowestPrice in minimum..maximum }

            //sorting
            list = if(sortedA_Z)
                list.sortedBy { it.lowestPrice }
            else
                list.sortedByDescending { it.lowestPrice }

            //adapter
            val adapter = ProductAdapter(this, list)
            listView.adapter = adapter
        }







        findViewById<EditText>(R.id.toolbar_search).addTextChangedListener {
            val searchText = it.toString()

            var list = mutableListOf<Product>()
            for(item in productList){
                if(item.modelName.toLowerCase().contains(searchText.toLowerCase())
                    && item.lowestPrice in minimum..maximum){
                    list.add(item)
                }
            }

            if(sortedA_Z)
                list = list.sortedBy { item -> item.lowestPrice }.toMutableList()
            else
                list = list.sortedByDescending { item -> item.lowestPrice }.toMutableList()

            val adapter = ProductAdapter(this, list)
            listView.adapter = adapter
        }

//        findViewById<RadioGroup>(R.id.rGroup).setOnCheckedChangeListener{
//                _, i ->
//            if(i==R.id.radioButton){
//                buildList(0)
//            }else if(i==R.id.radioButton2){
//                buildList(1)
//            }
//        }

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

//        fun rewriteProductList() {
//            var list = productList
//            if (search) {
//                var tempList = list
//                list.clear()
//                for(item in tempList){
//                    if(item.modelName.toLowerCase().contains(searchV.toLowerCase())){
//                        list.add(item)
//                    }
//                }
//            }
//
//            if (filterA) {
//                var tempList = list
//                list.clear()
//                for(item in tempList){
//                    if(item.lowestPrice > filterAV){
//                        list.add(item)
//                    }
//                }
//            }
//            if (filterB) {
//                var tempList = list
//                list.clear()
//                for(item in tempList){
//                    if(item.lowestPrice < filterBV){
//                        list.add(item)
//                    }
//                }
//            }
//            if (sort) {
//                if (sortV == 0)
//                {
//                    list = list.sortedBy { it.lowestPrice }
//
//
//                }
//            }
//        }

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