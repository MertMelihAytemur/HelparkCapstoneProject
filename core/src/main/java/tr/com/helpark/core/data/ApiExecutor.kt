package tr.com.helpark.core.data

import retrofit2.Response
import tr.com.helpark.core.data.remote.ApiError
import tr.com.helpark.core.data.remote.ApiResult
import tr.com.helpark.core.util.Constants
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * This class is responsible for executing API calls and handling the response.
 * It returns an [ApiResult] which can be either [ApiResult.Success] or [ApiResult.Error].
 */
interface ApiExecutor {
    suspend fun <T> execute(
        call: suspend () -> Response<T>
    ): ApiResult<T> {
        val response: Response<T>
        try {
            response = call.invoke()
            return if (response.isSuccessful) {
                ApiResult.Success(response.body())
            } else {
                when (response.code()) {
                    Constants.ERROR_CODE_SERVER -> ApiResult.Error(
                        ApiError.Authentication(response.errorBody())
                    )

                    else -> ApiResult.Error(ApiError.Server(response.errorBody()))
                }
            }
        } catch (exception: Exception) {
            return when (exception) {
                is ConnectException, is UnknownHostException -> ApiResult.Error(
                    ApiError.NoInternet(exception.message)
                )

                else -> ApiResult.Error(ApiError.IO(exception.message))
            }
        }
    }
}