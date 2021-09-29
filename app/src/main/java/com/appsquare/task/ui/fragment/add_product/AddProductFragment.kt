package com.appsquare.task.ui.fragment.add_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appsquare.task.data.ApiStatus
import com.appsquare.task.databinding.AddProductFragmentBinding
import com.appsquare.task.helper.ResponseErrorHandler
import com.appsquare.task.helper.isEditTextValid
import com.appsquare.task.helper.isNameValid
import com.appsquare.task.helper.isNumberValid
import com.kayan.voicechat.helper.SharedHelper
import org.koin.android.ext.android.inject

class AddProductFragment : Fragment() {


    private val viewModel by inject<AddProductViewModel>()
    private lateinit var binding: AddProductFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddProductFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpObserver()
        binding.apply {

            btnAddProduct.setOnClickListener {
                if (editTextTitle.isNameValid() &&
                    editTextPrice.isNumberValid() && editTextQuantity.isNumberValid()) {


                    viewModel.addProduct(
                        token = SharedHelper.getString(root.context, SharedHelper.TOKEN),
                        name = editTextTitle.text.toString(), price = editTextPrice.text.toString(),
                        quantity = editTextQuantity.text.toString()
                    )
                }
            }
        }

    }

    private fun setUpObserver() {
        viewModel.addProductLivedata.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    findNavController().navigateUp()
                }
                ApiStatus.ERROR -> {
                    activity?.let {
                        ResponseErrorHandler.handleErrorMessage(
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
        binding.loadingFrame.isVisible = loading
    }

}