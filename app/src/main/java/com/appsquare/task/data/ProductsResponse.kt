package com.appsquare.task.data

import com.google.gson.annotations.SerializedName

data class ProductsResponse(

	@field:SerializedName("data")
	val data: ArrayList<ProductItem?>? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ProductItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("restaurant_id")
	val restaurantId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
