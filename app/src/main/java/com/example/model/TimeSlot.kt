package com.example.model

enum class SlotPeriod(val label: String) {
  MORNING("Morning"),
  AFTERNOON("Afternoon"),
  EVENING("Evening")
}

data class TimeSlot(
  val id: String,
  val doctorId: String,
  val date: String, // YYYY-MM-DD
  val time: String, // "09:00 AM"
  val period: SlotPeriod,
  val tokenNumber: Int,
  val isBooked: Boolean = false,
  val isBlocked: Boolean = false
)
