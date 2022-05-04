package com.mykolaonyshchuk.pricecompare

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class ListActivity : AppCompatActivity() {
    val dbHelperObj = DbHelper()
    val productList = dbHelperObj.getProductList()
    var finalList = mutableListOf<Product>()


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

            if (list.isNotEmpty()) {
                finalList = list.toList() as MutableList<Product>
            } else {
                finalList.clear()
                Toast.makeText(applicationContext, "Not found", Toast.LENGTH_SHORT).show()
            }
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

            if (list.isNotEmpty()) {
                finalList = list.toList() as MutableList<Product>
            } else {
                finalList.clear()
                Toast.makeText(applicationContext, "Not found", Toast.LENGTH_SHORT).show()
            }
        }

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_phones_and_smartphones -> Toast.makeText(applicationContext, "Clicked Phones and smartphones", Toast.LENGTH_SHORT).show()
                R.id.nav_mobiles -> {
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            true

        }

        val adapter = ProductAdapter(this, productList)
        listView.adapter = adapter

        val context = MyApplication.getAppContext()
        listView.setOnItemClickListener { _, _, position, _ ->
            var selectedProduct : Product
            if (finalList.isEmpty()) {
                selectedProduct = productList[position]
            } else {
                selectedProduct = finalList[position]
            }

            val apiKey = "VWDLvFSSpC06mSFgCxWXGJQgqfdA5CUvKKY"
            var dbHelper = DbHelper()
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("productObj", selectedProduct.id)
            startActivity(intent)
        }

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}