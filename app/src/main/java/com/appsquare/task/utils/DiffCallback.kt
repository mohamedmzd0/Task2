package com.appsquare.task.utils

import androidx.recyclerview.widget.DiffUtil
import com.appsquare.task.data.ProductItem


class DiffCallback(private val newItems: ArrayList<ProductItem?>?, private var oldItems: ArrayList<ProductItem?>?) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems?.size?:0
    }

    override fun getNewListSize(): Int {
        return newItems?.size?:0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems?.get(oldItemPosition)?.id == newItems?.get(newItemPosition)?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems?.get(oldItemPosition)?.equals(newItems?.get(newItemPosition)) == true
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}