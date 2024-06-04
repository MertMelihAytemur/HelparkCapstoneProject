package com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model

enum class ReservationStatusType(val value: Int) {
    NOT_EXIST(-2),
    CANCELLED(-1),
    PENDING(0),
    CONFIRMED(1),
    ARRIVED(2);

    companion object {
        fun fromValue(value: Int): ReservationStatusType {
            return entries.firstOrNull { it.value == value } ?: NOT_EXIST
        }
    }
}