package com.appsquare.task.ui.fragment.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appsquare.task.R
import com.appsquare.task.adapter.ProductItemAction
import com.appsquare.task.adapter.ProductsAdapter
import com.appsquare.task.data.ApiResources
import com.appsquare.task.data.ApiStatus
import com.appsquare.task.data.StatusResponse
import com.appsquare.task.databinding.ProductsFragmentBinding
import com.appsquare.task.helper.PaginationScrollListener
import com.appsquare.task.helper.ResponseErrorHandler.Companion.handleErrorMessage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kayan.voicechat.helper.SharedHelper
import org.koin.android.ext.android.inject

class ProductsFragment : Fragment(), ProductItemAction {
/*

Total pages and current page not attached to response ,
i need to check the current page is first - > add and update current list , else append
if total == current -- > pagination ended


 */

    private val viewModel by inject<ProductsViewModel>()
    private lateinit var binding: ProductsFragmentBinding
    private val adapter = ProductsAdapter(this@ProductsFragment)
    private var currentPage = 1
    private var lastPage = 1
    private var isLoading = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            recyclerViewProducts.adapter = adapter

            currentPage = 1
            isLoading = true
            viewModel.getProducts(currentPage*10)
            setUpObserver()


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter.filter(searchView.query)
                    return true
                }
            })
            btnAdd.setOnClickListener {
                findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
            }


            recyclerViewProducts.layoutManager?.let {
                recyclerViewProducts.addOnScrollListener(object : PaginationScrollListener(it) {
                    override fun isLastPage(): Boolean {
                        return currentPage == lastPage
                    }

                    override fun isLoading(): Boolean {
                        return isLoading
                    }

                    override fun loadMoreItems() {
                        currentPage++
                        viewModel.getProducts(currentPage*10)
                    }


                })

            }
        }
    }

    private fun setUpObserver() {
        viewModel.productsLivedata.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    // must have total of pages and current page
                    adapter.setAll(response.data?.data)
                    binding.emptyTextView.isVisible = response.data?.data.isNullOrEmpty()
                }
                ApiStatus.ERROR -> {
                    activity?.let {
                        handleErrorMessage(
                            activity = it,
                            responseCode = response.responseCode,
                            message = response.message
                        )
                    }
                }
            }
            setLoading(response.status == ApiStatus.LOADING)
        }
    }

    private fun setLoading(loading: Boolean) {
        //animation if empty only
        isLoading = loading
        binding.shimmer.isVisible = loading && (adapter.itemCount == 0)
        if (loading && (adapter.itemCount == 0))
            binding.shimmer.startShimmer()
        else
            binding.shimmer.stopShimmer()

    }

    override fun deleteItem(position: Int, productID: Int, name: String?) {

        context?.let { context ->
            MaterialAlertDialogBuilder(context)
                .setMessage("Delete $name")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.deleteProduct(
                        token = SharedHelper.getString(context, SharedHelper.TOKEN),
                        productID = productID
                    ).observe(viewLifecycleOwner) { response ->

                       handleDeleteResponse(response,position)
                        setLoading(response.status == ApiStatus.LOADING)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun handleDeleteResponse(
        response: ApiResources<StatusResponse>?,
        position: Int
    ) {
        when (response?.status) {
            ApiStatus.SUCCESS -> {
                this.adapter.removeItem(position)
                binding.emptyTextView.isVisible = this.adapter.itemCount == 0
            }
            ApiStatus.ERROR -> {
                activity?.let {
                    handleErrorMessage(
                        activity = it,
                        responseCode = response.responseCode,
                        message = response.message
                    )
                }
            }
        }
    }


}