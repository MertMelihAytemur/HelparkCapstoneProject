package tr.com.helpark.core.data.remote

sealed interface ApiResult<out T>{

    class Success<T>(val response : T?) : ApiResult<T>

    class Error(val error : ApiError) : ApiResult<Nothing>
}