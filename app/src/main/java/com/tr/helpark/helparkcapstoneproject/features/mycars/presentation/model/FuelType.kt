package com.tr.helpark.helparkcapstoneproject.features.mycars.presentation.model

enum class FuelType(val id: Int, val description: String) {
    GASOLINE(1, "Gasoline"),
    DIESEL(2, "Diesel"),
    ELECTRIC(3, "Electric"),
    HYBRID(4, "Hybrid"),
    LPG(5, "LPG"),
    CNG(6, "CNG"),
    HYDROGEN(7, "Hydrogen");

    companion object {
        fun fromId(id: Int): FuelType? {
            return entries.find { it.id == id }
        }

        fun fromDescription(description: String): FuelType? {
            return entries.find { it.description.equals(description, ignoreCase = true) }
        }
    }
}