package com.mykolaonyshchuk.pricecompare

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
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

        if (convertView == null) {

            view = inflater.inflate(R.layout.list_item, parent, false)

            holder = ViewHolder()
            holder.thumbnailImageView = view.findViewById(R.id.product_list_thumbnail) as ImageView
            holder.titleTextView = view.findViewById(R.id.product_list_title) as TextView
            holder.detailTextView = view.findViewById(R.id.product_list_detail) as TextView
            holder.rupeeSymbol = view.findViewById(R.id.product_list_symbol) as ImageView
            view.tag = holder

        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val titleTextView = holder.titleTextView
        val detailTextView = holder.detailTextView
        val thumbnailImageView = holder.thumbnailImageView
        val rupeeSymbol = holder.rupeeSymbol

        val product = getItem(position) as Product

        titleTextView.text = product.modelName
        detailTextView.text = product.lowestPrice.toString()

        Picasso.with(context).load(product.imageUrl).placeholder(R.drawable.ic_baseline_smartphone_48).into(thumbnailImageView)

        return view
    }

    private class ViewHolder {
        lateinit var titleTextView: TextView
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