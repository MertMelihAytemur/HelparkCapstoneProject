package com.tr.helpark.helparkcapstoneproject.common.helper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.tr.helpark.helparkcapstoneproject.features.reservation.presentation.model.ReservationStatusType
import javax.inject.Inject

class FirebaseHelper @Inject constructor(
    private val database: DatabaseReference
) {

    fun addOrUpdateReservationStatus(userId: String, status: Int) {
        val userRef = database.child("users").child(userId).child("reservation_status")

        userRef.setValue(status).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Reservation status successfully added/updated.")
            } else {
                println("Failed to add/update reservation status: ${task.exception?.message}")
            }
        }
    }

    fun listenToReservationStatus(userId: String, statusListener: (ReservationStatusType) -> Unit) {
        val userRef = database.child("users").child(userId).child("reservation_status")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val statusValue =
                    snapshot.getValue(Int::class.java) ?: ReservationStatusType.NOT_EXIST.value
                val status = ReservationStatusType.fromValue(statusValue)
                statusListener(status)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to listen to reservation status: ${error.message}")
            }
        })
    }
}