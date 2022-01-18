package com.example.pricecompare

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"


class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var button: Button
    var jsonString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        findViewById<AppCompatImageButton>(R.id.toolbar_menu).setOnClickListener {
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            }else{
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)


        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_phones_and_smartphones -> Toast.makeText(
                    applicationContext,
                    "Clicked Phones and smartphones",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.nav_mobiles -> {
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lastSync = sharedPreferences.getLong("lastProductListSync", -1)

        var dbHelper = FeedReaderDbHelper(this)
        if (TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - lastSync) > 60 && lastSync != -1L) {
            val dbLocal = dbHelper.writableDatabase
            dbLocal.delete(FeedReaderContract.FeedProductEntry.TABLE_NAME, null, null)
            dbLocal.delete(FeedReaderContract.FeedCategoryEntry.TABLE_NAME, null, null)
        }

        val dbR = dbHelper.readableDatabase
        val cursor = dbR.query(
            FeedReaderContract.FeedProductEntry.TABLE_NAME,
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
            for (i in 1..20) {
                dbH.loadProductList(i)
            }
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putLong("lastProductListSync", System.currentTimeMillis())
            editor.apply()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}