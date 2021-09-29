package com.appsquare.task.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appsquare.task.R
import com.appsquare.task.data.ProductItem
import com.appsquare.task.databinding.ItemProductBinding
import com.appsquare.task.utils.DiffCallback
import com.bumptech.glide.Glide
import com.google.zxing.WriterException
import org.json.JSONObject


private const val TAG = "ProductsAdapter"

class ProductsAdapter(private val productItemAction: ProductItemAction) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>(), Filterable {

    private val products = ArrayList<ProductItem?>()
    private val productsCopy = ArrayList<ProductItem?>()

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(productItem: ProductItem?) {
            binding.apply {
                productTitle.text = productItem?.name
                productPrice.text = " ${productItem?.price} $"
                Glide.with(productImage).load(productItem?.image)
                    .placeholder(R.drawable.not_found)
                    .override(500, 500)
                    .into(productImage)
                productItem?.id?.let { generateCode(qrCodeImage, it) }
            }
        }

        init {
            binding.delete.setOnClickListener {
                if (adapterPosition < 0)
                    return@setOnClickListener
                products[adapterPosition]?.id?.let { it1 ->
                    productItemAction.deleteItem(
                        position = adapterPosition,
                        productID = it1, products[adapterPosition]?.name
                    )
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        holder.bindItem(products[p])
    }

    fun setAll(data: ArrayList<ProductItem?>?) {

        productsCopy.clear()
        if (data != null)
            productsCopy.addAll(data)

        if (products.isNullOrEmpty()) {
            //no need to check
            data?.let { this.products.addAll(it) }
            notifyItemRangeInserted(0, data?.size ?: 0)
        } else {
            val diffResult =
                DiffUtil.calculateDiff(DiffCallback(oldItems = this.products, newItems = data),true)
            products.clear()
            if (data != null) {
                products.addAll(data)
            }
            diffResult.dispatchUpdatesTo(this)
        }
    }

    fun removeItem(position: Int) {
        //required for filtering
        for (i in 0..productsCopy.size) {
            if (productsCopy[i]?.id == products[position]?.id) {
                productsCopy.removeAt(i)
                break
            }
        }
        products.removeAt(position)
        notifyItemRemoved(position)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                products.clear()
                if (constraint.isNullOrBlank()) {
                    products.addAll(productsCopy)
                } else {
                    val searchResults =
                        productsCopy.filter { it?.name?.contains(constraint) == true }
                    products.addAll(searchResults)
                }
                return filterResults.also {
                    it.values = products
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()

            }
        }
    }

    private fun generateCode(imageView: ImageView, productID: Int) {
        // json is important for any updating in future
        val json = JSONObject()
        json.put("product_id", productID)
        val qrgEncoder = QRGEncoder(json.toString(), null, QRGContents.Type.TEXT, 200)
        qrgEncoder.colorBlack = Color.BLACK
        qrgEncoder.colorWhite = Color.WHITE
        try {
            imageView.setImageBitmap(qrgEncoder.bitmap)
        } catch (e: WriterException) {
            Log.e(TAG, e.toString())
        }
    }

}

interface ProductItemAction {
    fun deleteItem(position: Int, productID: Int, title: String?)
}