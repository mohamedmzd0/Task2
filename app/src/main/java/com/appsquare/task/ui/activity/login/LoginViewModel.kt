import android.util.Log
import androidx.lifecycle.*
import com.appsquare.task.api.RetrofitServices
import com.appsquare.task.data.ApiResources
import com.appsquare.task.data.LoginResponse
import com.appsquare.task.data.StatusResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

private const val TAG = "LoginViewmodel"

class LoginViewModel(private val apiServices: RetrofitServices) : ViewModel() {
    val loginLivedata = MutableLiveData<ApiResources<LoginResponse>>()

    fun login(email: String, password: String) {
        loginLivedata.value = ApiResources.Loading(true)
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                apiServices.login(
                    email = email, password = password
                )
            }.onSuccess { response ->
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        loginLivedata.value = ApiResources.Success(
                            _data = response.body(),
                            _responseCode = response.code()
                        )
                    } else {
                        val errorMsg = response.errorBody()?.string()
                        response.errorBody()?.close()
                        loginLivedata.value = ApiResources.Error(
                            exception = errorMsg.toString(),
                            _responseCode = response.code()
                        )
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main) {
                    loginLivedata.value = ApiResources.Error(exception = it.message.toString())
                }
            }
        }
    }


}