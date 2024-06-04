package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model

object ReservationModel{
    var carPlateId : Int? = null
    var userId : Int? = null
    var parkId : Int? = null
    var hire : Float? = null
    var resTime : Int? = null
}

fun resetReservationModel(){
    ReservationModel.carPlateId = null
    ReservationModel.userId = null
    ReservationModel.parkId = null
    ReservationModel.hire = null
    ReservationModel.resTime = null
}


