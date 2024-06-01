package com.tr.helpark.helparkcapstoneproject.features.profile.data.remote

import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response.AddBalanceDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response.DeleteUserAccountDto
import com.tr.helpark.helparkcapstoneproject.features.profile.data.dto.response.GetProfileResponseDto
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @GET(END_POINT_GET_PROFILE)
    suspend fun getProfile(
        @Path("userId") userId: String
    ) : Response<GetProfileResponseDto>

    @POST(END_POINT_ADD_BALANCE)
    suspend fun addBalance(
        @Query("id") id: Int,
        @Query("balance") balance: Float
    ) : Response<AddBalanceDto>

    @DELETE(END_POINT_DELETE_USER_ACCOUNT)
    suspend fun deleteUserAccount(
        @Query("userId") userId: Int
    ): Response<DeleteUserAccountDto>

    private companion object{
        const val END_POINT_GET_PROFILE = "/User/GetAllDataFromUser/{userId}"
        const val END_POINT_ADD_BALANCE = "/User/AddBalance"
        const val END_POINT_DELETE_USER_ACCOUNT = "/User/DeactiveUserFromUserId"
    }
}