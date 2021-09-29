package com.appsquare.task.di

import LoginViewModel
import com.appsquare.task.api.RetrofitServices
import com.appsquare.task.api.retrofit
import com.appsquare.task.ui.fragment.add_product.AddProductViewModel
import com.appsquare.task.ui.fragment.products.ProductsViewModel
import org.koin.dsl.module

val retrofitModule = module {
    single { retrofit("https://android-training.appssquare.com/api/").create(RetrofitServices::class.java) }
}

val viewModelModules= module {
    factory { LoginViewModel(get()) }
    single { ProductsViewModel(get()) }
    factory { AddProductViewModel(get()) }
}