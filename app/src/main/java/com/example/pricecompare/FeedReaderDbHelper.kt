package com.example.pricecompare

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CATEGORY_CREATE_ENTRIES)
        db.execSQL(SQL_BRAND_CREATE_ENTRIES)
        db.execSQL(SQL_PRODUCT_CREATE_ENTRIES)
        db.execSQL(SQL_SPECS_CREATE_ENTRIES)
        db.execSQL(SQL_SHOPS_CREATE_ENTRIES)
        db.execSQL(SQL_PRICES_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "products.db"
    }
}

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedCategoryEntry : BaseColumns {
        const val TABLE_NAME = "Category"
        //const val COLUMN_ID = "id"
        const val COLUMN_CATEGORY_NAME = "name"
    }

    object FeedProductEntry : BaseColumns {
        const val TABLE_NAME = "Product"
        const val COLUMN_ID = "id"
        const val COLUMN_BRAND_ID = "brandId"
        const val COLUMN_CATEGORY_ID = "categoryId"
        const val COLUMN_MODEL_NAME = "modelName"
        const val COLUMN_LOWEST_PRICE = "lowestPrice"
        const val COLUMN_IMAGE_URL = "imageUrl"
        const val COLUMN_RATING = "rating"
    }

    object FeedBrandEntry : BaseColumns {
        const val TABLE_NAME = "Brand"
        //const val COLUMN_ID = "id"
        const val COLUMN_BRAND_NAME = "name"
    }

    object FeedSpecsEntry : BaseColumns {
        const val TABLE_NAME = "Specs"
        const val COLUMN_ID = "id"
        const val COLUMN_MAIN_SPECS = "mainSpecs"
        const val COLUMN_REAR_CAMERA = "rearCamera"
        const val COLUMN_FRONT_CAMERA = "frontCamera"
        const val COLUMN_SCREEN_RESOLUTION = "screenResolution"
        const val COLUMN_SCREEN_SIZE = "screenSize"
        const val COLUMN_PROCESSOR = "processor"
        const val COLUMN_INTERNAL_MEMORY = "internalMemory"
        const val COLUMN_RAM = "ram"
        const val COLUMN_BATTERY = "battery"
    }

    object FeedShopsEntry : BaseColumns {
        const val TABLE_NAME = "Shop"
        //const val COLUMN_ID = "id"
        const val COLUMN_SHOP_NAME = "name"
    }

    object FeedPricesEntry : BaseColumns {
        const val TABLE_NAME = "Prices"
        //const val COLUMN_ID = "id"
        const val COLUMN_PRODUCT_ID = "deviceId"
        const val COLUMN_SHOP_ID = "shopId"
        const val COLUMN_PRICE = "price"
    }
}

private const val SQL_CATEGORY_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedCategoryEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedCategoryEntry.COLUMN_CATEGORY_NAME} TEXT)"

private const val SQL_BRAND_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedBrandEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedBrandEntry.COLUMN_BRAND_NAME} TEXT)"

private const val SQL_PRODUCT_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedProductEntry.TABLE_NAME} (" +
            "${FeedReaderContract.FeedProductEntry.COLUMN_ID} TEXT PRIMARY KEY," +
            "${FeedReaderContract.FeedProductEntry.COLUMN_BRAND_ID} INTEGER," +
            "${FeedReaderContract.FeedProductEntry.COLUMN_CATEGORY_ID} INTEGER," +
            "${FeedReaderContract.FeedProductEntry.COLUMN_MODEL_NAME} TEXT, " +
            "${FeedReaderContract.FeedProductEntry.COLUMN_LOWEST_PRICE} INTEGER, " +
            "${FeedReaderContract.FeedProductEntry.COLUMN_IMAGE_URL} TEXT, " +
            "${FeedReaderContract.FeedProductEntry.COLUMN_RATING} REAL, " +
            "FOREIGN KEY(${FeedReaderContract.FeedProductEntry.COLUMN_BRAND_ID}) REFERENCES ${FeedReaderContract.FeedBrandEntry.TABLE_NAME}(${BaseColumns._ID}), " +
            "FOREIGN KEY(${FeedReaderContract.FeedProductEntry.COLUMN_CATEGORY_ID}) REFERENCES ${FeedReaderContract.FeedCategoryEntry.TABLE_NAME}(${BaseColumns._ID}))"

private const val SQL_SPECS_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedSpecsEntry.TABLE_NAME} (" +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_ID} TEXT PRIMARY KEY," +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_MAIN_SPECS} TEXT, " +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_REAR_CAMERA} TEXT, " +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_FRONT_CAMERA} TEXT, " +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_RESOLUTION} TEXT, " +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_SCREEN_SIZE} TEXT, " +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_PROCESSOR} TEXT" +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_INTERNAL_MEMORY} TEXT, " +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_RAM} TEXT, " +
            "${FeedReaderContract.FeedSpecsEntry.COLUMN_BATTERY} TEXT)"

private const val SQL_SHOPS_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedShopsEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedShopsEntry.COLUMN_SHOP_NAME} TEXT)"

private const val SQL_PRICES_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedPricesEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedPricesEntry.COLUMN_PRODUCT_ID} TEXT, " +
            "${FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_ID} INTEGER, " +
            "${FeedReaderContract.FeedPricesEntry.COLUMN_PRICE} INTEGER, " +
            "FOREIGN KEY(${FeedReaderContract.FeedPricesEntry.COLUMN_PRODUCT_ID}) REFERENCES ${FeedReaderContract.FeedProductEntry.TABLE_NAME}(${FeedReaderContract.FeedProductEntry.COLUMN_ID}), " +
            "FOREIGN KEY(${FeedReaderContract.FeedPricesEntry.COLUMN_SHOP_ID}) REFERENCES ${FeedReaderContract.FeedShopsEntry.TABLE_NAME}(${BaseColumns._ID}))" /////////////////


private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedCategoryEntry.TABLE_NAME}"