package com.appsquare.task.api

import com.appsquare.task.data.LoginResponse
import com.appsquare.task.data.ProductsResponse
import com.appsquare.task.data.StatusResponse
import retrofit2.Response
import retrofit2.http.*

interface RetrofitServices {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>


    @GET("products")
    suspend fun getProducts(@Query("skip") page:Int): Response<ProductsResponse>


    @FormUrlEncoded
    @POST("products")
    suspend fun addProduct(
        @Header("Authorization") token: String?,
        @Field("name") name: String,
        @Field("price") price: String,
        @Field("quantity") quantity: String
    ): Response<StatusResponse>

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String?,
        @Path("id") id: Int
    ): Response<StatusResponse>

}