package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HealthcareRepository
import com.example.model.AppointmentStatus
import com.example.model.ConsultationMode
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context matches Get Well Soon`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Get Well Soon", appName)
  }

  @Test
  fun `verify initial doctors and clinics are populated`() {
    val repo = HealthcareRepository()
    assertTrue(repo.doctors.value.isNotEmpty())
    assertTrue(repo.clinics.value.isNotEmpty())
    val doctor = repo.doctors.value.first()
    assertNotNull(doctor.name)
    assertTrue(doctor.consultationFee > 0)
  }

  @Test
  fun `atomic booking prevents double booking same slot`() {
    val repo = HealthcareRepository()
    val doctor = repo.doctors.value.first()
    val date = "2026-11-01"
    val time = "10:00 AM"

    // First booking should succeed
    val result1 = repo.bookAppointment(
      patientId = "pat-1001",
      patientName = "Alex Morgan",
      patientPhone = "+1 555-234-8901",
      doctor = doctor,
      date = date,
      time = time,
      tokenNumber = 3,
      mode = ConsultationMode.IN_CLINIC,
      reason = "General Checkup",
      notes = "No prior conditions"
    )
    assertTrue(result1.isSuccess)
    val bookedApt = result1.getOrNull()
    assertNotNull(bookedApt)
    assertEquals(AppointmentStatus.CONFIRMED, bookedApt?.status)

    // Second booking attempt on exact same doctor, date and time MUST fail (double booking prevention)
    val result2 = repo.bookAppointment(
      patientId = "pat-9999",
      patientName = "Second Patient",
      patientPhone = "+1 555-999-9999",
      doctor = doctor,
      date = date,
      time = time,
      tokenNumber = 3,
      mode = ConsultationMode.IN_CLINIC,
      reason = "Urgent consultation",
      notes = ""
    )
    assertTrue(result2.isFailure)
  }

  @Test
  fun `doctor consultation status workflow updates correctly`() {
    val repo = HealthcareRepository()
    val doctor = repo.doctors.value.first()
    val aptResult = repo.bookAppointment(
      patientId = "pat-1001",
      patientName = "Alex Morgan",
      patientPhone = "+1 555-234-8901",
      doctor = doctor,
      date = "2026-11-02",
      time = "11:00 AM",
      tokenNumber = 5,
      mode = ConsultationMode.IN_CLINIC,
      reason = "Follow-up",
      notes = "Review lab results"
    )
    val aptId = aptResult.getOrThrow().id

    // Check-in
    repo.updateAppointmentStatus(aptId, AppointmentStatus.CHECKED_IN)
    assertEquals(AppointmentStatus.CHECKED_IN, repo.appointments.value.first { it.id == aptId }.status)

    // In consultation
    repo.updateAppointmentStatus(aptId, AppointmentStatus.IN_CONSULTATION)
    assertEquals(AppointmentStatus.IN_CONSULTATION, repo.appointments.value.first { it.id == aptId }.status)

    // Complete with clinical notes and prescription
    val notes = "Patient in good cardiovascular health."
    val rx = "Aspirin 81mg"
    repo.updateAppointmentStatus(aptId, AppointmentStatus.COMPLETED, notes = notes, prescriptions = rx)

    val completedApt = repo.appointments.value.first { it.id == aptId }
    assertEquals(AppointmentStatus.COMPLETED, completedApt.status)
    assertEquals(notes, completedApt.doctorNotes)
    assertEquals(rx, completedApt.prescriptionSummary)
  }
}
