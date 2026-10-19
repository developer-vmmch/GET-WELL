package com.example.model

enum class ConsultationMode(val label: String) {
  IN_CLINIC("In-Clinic"),
  VIDEO("Video Call"),
  BOTH("In-Clinic & Video")
}

data class Doctor(
  val id: String,
  val name: String,
  val title: String,
  val specialty: Specialty,
  val qualifications: String,
  val clinicId: String,
  val clinicName: String,
  val experienceYears: Int,
  val rating: Double,
  val reviewCount: Int,
  val consultationFee: Double,
  val consultationMode: ConsultationMode = ConsultationMode.BOTH,
  val bio: String,
  val nextAvailableSlot: String,
  val verified: Boolean = true,
  val avatarColorHex: Long = 0xFF0284C7
)
