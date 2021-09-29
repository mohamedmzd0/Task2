package com.appsquare.task.data

import com.google.gson.annotations.SerializedName

data class StatusResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
