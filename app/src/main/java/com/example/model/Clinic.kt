package com.example.model

data class Clinic(
  val id: String,
  val name: String,
  val address: String,
  val city: String,
  val phone: String,
  val openingHours: String,
  val rating: Double,
  val reviewCount: Int,
  val services: List<String>,
  val verified: Boolean = true,
  val distanceKm: Double = 1.2
)
