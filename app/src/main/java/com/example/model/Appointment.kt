package com.example.model

enum class AppointmentStatus(val label: String) {
  PENDING("Pending"),
  CONFIRMED("Confirmed"),
  CHECKED_IN("Checked In"),
  IN_CONSULTATION("In Consultation"),
  COMPLETED("Completed"),
  CANCELLED("Cancelled")
}

data class Appointment(
  val id: String,
  val patientId: String,
  val patientName: String,
  val patientPhone: String = "+1 555-0192",
  val patientAge: Int = 32,
  val patientGender: String = "Male",
  val doctorId: String,
  val doctorName: String,
  val doctorSpecialty: String,
  val clinicName: String,
  val clinicAddress: String,
  val date: String,
  val time: String,
  val tokenNumber: Int,
  val consultationMode: ConsultationMode = ConsultationMode.IN_CLINIC,
  val visitReason: String = "General Checkup",
  val symptomsNote: String = "",
  val status: AppointmentStatus = AppointmentStatus.CONFIRMED,
  val consultationFee: Double = 65.0,
  val doctorNotes: String = "",
  val prescriptionSummary: String = "",
  val createdAtMillis: Long = System.currentTimeMillis()
)
