package com.appsquare.task.ui.fragment.products

import android.graphics.pdf.PdfDocument
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsquare.task.api.RetrofitServices
import com.appsquare.task.data.ApiResources
import com.appsquare.task.data.ProductsResponse
import com.appsquare.task.data.StatusResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsViewModel(private val apiServices: RetrofitServices) : ViewModel() {
    val productsLivedata = MutableLiveData<ApiResources<ProductsResponse>>()

    fun getProducts(page: Int) {
        productsLivedata.value = ApiResources.Loading(true)
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                apiServices.getProducts(page)
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        productsLivedata.value = ApiResources.Success(
                            _data = response.body(),
                            _responseCode = response.code()
                        )
                    } else {
                        val errorMsg = response.errorBody()?.string()
                        response.errorBody()?.close()
                        productsLivedata.value = ApiResources.Error(
                            exception = errorMsg.toString(),
                            _responseCode = response.code()
                        )
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    productsLivedata.value = ApiResources.Error(exception = it.message.toString())
                }
            }
        }
    }

    fun deleteProduct(
        token: String?,
        productID: Int
    ): MutableLiveData<ApiResources<StatusResponse>> {
        val deleteProductLivedata = MutableLiveData<ApiResources<StatusResponse>>()
        deleteProductLivedata.value = ApiResources.Loading(true)
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                apiServices.deleteProduct(token = token, id = productID)
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        deleteProductLivedata.value = ApiResources.Success(
                            _data = response.body(),
                            _responseCode = response.code()
                        )
                    } else {
                        val errorMsg = response.errorBody()?.string()
                        response.errorBody()?.close()
                        deleteProductLivedata.value = ApiResources.Error(
                            exception = errorMsg.toString(),
                            _responseCode = response.code()
                        )
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    deleteProductLivedata.value =
                        ApiResources.Error(exception = it.message.toString())
                }
            }
        }
        return deleteProductLivedata
    }


/*    val liveData = flow {
        emit(ApiResources.Success(null))
        val response = apiServices.getProducts()
        if (response.isSuccessful) {
            emit(ApiResources.Success(response.body()))
        } else {
            val errorMsg = response.errorBody()?.string()
            response.errorBody()?.close()
            emit(ApiResources.Error(errorMsg.toString()))
        }
    }.asLiveData()*/

}


