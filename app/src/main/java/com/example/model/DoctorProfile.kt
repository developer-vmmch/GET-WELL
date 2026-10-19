package com.example.model

enum class DoctorStatus(val label: String) {
  AVAILABLE("Available"),
  IN_CONSULTATION("In Consultation"),
  ON_BREAK("On Break"),
  OFF_DUTY("Off Duty")
}

data class DoctorProfile(
  val id: String = "doc-01",
  val fullName: String = "Dr. Sarah Jenkins",
  val title: String = "MD, FACC",
  val specialty: Specialty = Specialty.CARDIOLOGY,
  val medicalLicenseNumber: String = "MED-US-89421",
  val clinicAffiliation: String = "Nova Heart & Vascular Institute",
  val phone: String = "+1 (555) 301-4490",
  val email: String = "s.jenkins@novamed.org",
  val experienceYears: Int = 14,
  val status: DoctorStatus = DoctorStatus.AVAILABLE,
  val slotDurationMinutes: Int = 20,
  val todayWorkingHours: String = "09:00 AM - 05:00 PM",
  val roomNumber: String = "Suite 302B"
)
