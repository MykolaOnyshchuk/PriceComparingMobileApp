package com.example.pricecompare

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.squareup.picasso.Picasso


class ProductAdapter(private val context: Context,
                     private val dataSource: List<Product>) : BaseAdapter(), Parcelable {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    constructor(parcel: Parcel) : this(
        TODO("context"),
        TODO("dataSource")
    ) {
    }

//    companion object {
//        private val LABEL_COLORS = hashMapOf(
//            "Low-Carb" to R.color.colorLowCarb,
//            "Low-Fat" to R.color.colorLowFat,
//            "Low-Sodium" to R.color.colorLowSodium,
//            "Medium-Carb" to R.color.colorMediumCarb,
//            "Vegetarian" to R.color.colorVegetarian,
//            "Balanced" to R.color.colorBalanced
//        )
//    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        // 1
        if (convertView == null) {

            // 2
            view = inflater.inflate(R.layout.list_item, parent, false)

            // 3
            holder = ViewHolder()
            holder.thumbnailImageView = view.findViewById(R.id.product_list_thumbnail) as ImageView
            holder.titleTextView = view.findViewById(R.id.product_list_title) as TextView
            //holder.subtitleTextView = view.findViewById(R.id.recipe_list_subtitle) as TextView
            holder.detailTextView = view.findViewById(R.id.product_list_detail) as TextView
            holder.rupeeSymbol = view.findViewById(R.id.product_list_symbol) as ImageView

            // 4
            view.tag = holder

//            view.setOnClickListener {
//                val intent = Intent(parent.context, ProductDetailsActivity::class.java)
//                parent.context.startActivity(intent)
//            }
        } else {
            // 5
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        // 6
        val titleTextView = holder.titleTextView
        //val subtitleTextView = holder.subtitleTextView
        val detailTextView = holder.detailTextView
        val thumbnailImageView = holder.thumbnailImageView
        val rupeeSymbol = holder.rupeeSymbol

        val recipe = getItem(position) as Product

        titleTextView.text = recipe.modelName
        //subtitleTextView.text = recipe.rating.toString()
        detailTextView.text = recipe.lowestPrice.toString()

        Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

//        val titleTypeFace = ResourcesCompat.getFont(context, R.font.josefinsans_bold)
//        titleTextView.typeface = titleTypeFace
//
//        val subtitleTypeFace = ResourcesCompat.getFont(context, R.font.josefinsans_semibolditalic)
//        subtitleTextView.typeface = subtitleTypeFace
//
//        val detailTypeFace = ResourcesCompat.getFont(context, R.font.quicksand_bold)
//        detailTextView.typeface = detailTypeFace

//        detailTextView.setTextColor(
//            //ContextCompat.getColor(context, LABEL_COLORS[recipe.lowestPrice] ?: R.color.colorPrimary))
//            ContextCompat.getColor(context, R.color.black))

        return view
    }

    private class ViewHolder {
        lateinit var titleTextView: TextView
        //lateinit var subtitleTextView: TextView
        lateinit var detailTextView: TextView
        lateinit var thumbnailImageView: ImageView
        lateinit var rupeeSymbol: ImageView
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductAdapter> {
        override fun createFromParcel(parcel: Parcel): ProductAdapter {
            return ProductAdapter(parcel)
        }

        override fun newArray(size: Int): Array<ProductAdapter?> {
            return arrayOfNulls(size)
        }
    }
}