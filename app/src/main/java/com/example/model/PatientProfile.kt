package com.example.model

data class PatientProfile(
  val id: String = "pat-1001",
  val fullName: String = "Alex Morgan",
  val phone: String = "+1 (555) 234-8901",
  val email: String = "alex.morgan@email.com",
  val bloodGroup: String = "O+",
  val dateOfBirth: String = "1994-06-15",
  val gender: String = "Male",
  val allergies: List<String> = listOf("Penicillin", "Peanuts"),
  val emergencyContact: String = "Sarah Morgan (+1 555-8912)",
  val selectedCity: String = "San Francisco, CA"
)
