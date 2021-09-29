package com.appsquare.task.ui.fragment.add_product

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsquare.task.BuildConfig
import com.appsquare.task.api.RetrofitServices
import com.appsquare.task.data.ApiResources
import com.appsquare.task.data.StatusResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "AddProductViewModel"

class AddProductViewModel(private val apiServices: RetrofitServices) : ViewModel() {

    val addProductLivedata = MutableLiveData<ApiResources<StatusResponse>>()

    fun addProduct(token: String?, name: String, price: String, quantity: String) {
        if (BuildConfig.DEBUG) {
            Log.d(
                TAG,
                "addProduct: \n token $token \n name $name \n price $price \n quantity $quantity "
            )
        }
        addProductLivedata.value = ApiResources.Loading(true)
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                apiServices.addProduct(
                    token = token,
                    name = name,
                    price = price,
                    quantity = quantity
                )
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        addProductLivedata.value = ApiResources.Success(
                            _data = response.body(),
                            _responseCode = response.code()
                        )
                    } else {
                        val errorMsg = response.errorBody()?.string()
                        response.errorBody()?.close()
                        addProductLivedata.value = ApiResources.Error(
                            exception = errorMsg.toString(),
                            _responseCode = response.code()
                        )
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    addProductLivedata.value = ApiResources.Error(exception = it.message.toString())
                }
            }
        }
    }


}