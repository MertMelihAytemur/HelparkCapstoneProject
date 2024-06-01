package com.tr.helpark.helparkcapstoneproject.features.mycars.data.remote

import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.request.AddNewCarRequestDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.response.AddNewCarDto
import com.tr.helpark.helparkcapstoneproject.features.mycars.data.dto.response.RemoveCarDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MyCarsService {

    @POST(END_POINT_ADD_PLATE)
    suspend fun addPlate(
        @Body addNewCarRequestDto: AddNewCarRequestDto
    ) : Response<AddNewCarDto>

    @POST(END_POINT_REMOVE_PLATE)
    suspend fun removePlate(
        @Query("plate") plate: String,
    ) : Response<RemoveCarDto>

    companion object{
        const val END_POINT_ADD_PLATE = "/Car/AddPlate"
        const val END_POINT_REMOVE_PLATE = "/Car/ActiveOrDeactiveCarPlate"
    }
}