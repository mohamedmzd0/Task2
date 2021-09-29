package com.appsquare.task.ui.activity.login

import LoginViewModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.appsquare.task.data.ApiStatus
import com.appsquare.task.databinding.ActivityLoginBinding
import com.appsquare.task.helper.ResponseErrorHandler
import com.appsquare.task.helper.isEmailValid
import com.appsquare.task.helper.isPasswordalid
import com.appsquare.task.ui.activity.home.HomeActivity
import com.kayan.voicechat.helper.SharedHelper
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by inject<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (!SharedHelper.getString(this, SharedHelper.TOKEN).isNullOrEmpty()) {
            goHomeActivity()
        }
        setUpObserver()
        binding.apply {
            loginButton.setOnClickListener {
                if (editTextEmail.isEmailValid() && editTextPassword.isPasswordalid()) {
                    viewModel.login(
                        editTextEmail.text.toString(),
                        password = editTextPassword.text.toString()
                    )
                }
            }
        }
    }

    private fun setUpObserver() {
        viewModel.loginLivedata.observe(this) { response ->
            when (response.status) {
                ApiStatus.SUCCESS -> {
                    SharedHelper.saveUser(
                        this,
                        response.data?.data,
                        response?.data?.token.toString()
                    )
                    goHomeActivity()

                }
                ApiStatus.ERROR -> {
                    ResponseErrorHandler.handleErrorMessage(
                        isLogin = true,
                        activity = this,
                        responseCode = response.responseCode,
                        message = response.message
                    )
                }
            }
            setLoading(response.status == ApiStatus.LOADING)
        }
    }

    private fun goHomeActivity() {
        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finishAffinity()
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loadingFrame.isVisible = isLoading
    }

}