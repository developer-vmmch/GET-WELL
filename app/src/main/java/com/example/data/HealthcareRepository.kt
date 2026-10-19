package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class HealthcareRepository {

  private val _clinics = MutableStateFlow<List<Clinic>>(emptyList())
  val clinics: StateFlow<List<Clinic>> = _clinics.asStateFlow()

  private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
  val doctors: StateFlow<List<Doctor>> = _doctors.asStateFlow()

  private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
  val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

  private val _patientProfile = MutableStateFlow(PatientProfile())
  val patientProfile: StateFlow<PatientProfile> = _patientProfile.asStateFlow()

  private val _doctorProfile = MutableStateFlow(DoctorProfile())
  val doctorProfile: StateFlow<DoctorProfile> = _doctorProfile.asStateFlow()

  private val _patientSession = MutableStateFlow(
    AuthSession(
      isAuthenticated = true,
      role = UserRole.PATIENT,
      userId = "pat-1001",
      userName = "Alex Morgan",
      token = "jwt_pat_sample_auth_token",
      emailOrPhone = "+1 (555) 234-8901"
    )
  )
  val patientSession: StateFlow<AuthSession> = _patientSession.asStateFlow()

  private val _doctorSession = MutableStateFlow(
    AuthSession(
      isAuthenticated = true,
      role = UserRole.DOCTOR,
      userId = "doc-01",
      userName = "Dr. Sarah Jenkins",
      token = "jwt_doc_sample_auth_token",
      emailOrPhone = "s.jenkins@novamed.org"
    )
  )
  val doctorSession: StateFlow<AuthSession> = _doctorSession.asStateFlow()

  // Dynamic slot booking map: "doctorId_date_time" -> isBooked
  private val bookedSlots = MutableStateFlow<Set<String>>(mutableSetOf())
  private val blockedSlots = MutableStateFlow<Set<String>>(mutableSetOf())

  init {
    seedInitialData()
  }

  private fun seedInitialData() {
    val initialClinics = listOf(
      Clinic(
        id = "cl-01",
        name = "Nova Heart & Vascular Institute",
        address = "742 Medical Center Blvd, Suite 400",
        city = "San Francisco, CA",
        phone = "+1 (555) 432-8800",
        openingHours = "08:00 AM - 07:00 PM",
        rating = 4.9,
        reviewCount = 384,
        services = listOf("Cardiology Consult", "ECG", "Echocardiogram", "Vascular Doppler", "Stress Test"),
        verified = true,
        distanceKm = 0.8
      ),
      Clinic(
        id = "cl-02",
        name = "Bayview Multispecialty Medical Center",
        address = "1200 Pacific Heights Ave",
        city = "San Francisco, CA",
        phone = "+1 (555) 890-1200",
        openingHours = "08:30 AM - 06:30 PM",
        rating = 4.8,
        reviewCount = 512,
        services = listOf("General Medicine", "Neurology", "Pediatrics", "Diagnostic Lab", "Vaccinations"),
        verified = true,
        distanceKm = 1.4
      ),
      Clinic(
        id = "cl-03",
        name = "Apex Orthopedics & Sports Care",
        address = "450 Mission Bay South",
        city = "San Francisco, CA",
        phone = "+1 (555) 781-9922",
        openingHours = "09:00 AM - 05:00 PM",
        rating = 4.7,
        reviewCount = 295,
        services = listOf("Joint Consultation", "Sports Rehabilitation", "X-Ray", "Spine Care"),
        verified = true,
        distanceKm = 2.1
      ),
      Clinic(
        id = "cl-04",
        name = "Luminary Dermatology & Aesthetics",
        address = "88 Post Street, 6th Floor",
        city = "San Francisco, CA",
        phone = "+1 (555) 670-3411",
        openingHours = "09:00 AM - 06:00 PM",
        rating = 4.9,
        reviewCount = 420,
        services = listOf("Skin Cancer Screening", "Acne Treatment", "Laser Therapy", "Eczema Management"),
        verified = true,
        distanceKm = 2.5
      )
    )
    _clinics.value = initialClinics

    val initialDoctors = listOf(
      Doctor(
        id = "doc-01",
        name = "Dr. Sarah Jenkins",
        title = "MD, FACC - Senior Cardiologist",
        specialty = Specialty.CARDIOLOGY,
        qualifications = "Harvard Medical School • 14 yrs exp",
        clinicId = "cl-01",
        clinicName = "Nova Heart & Vascular Institute",
        experienceYears = 14,
        rating = 4.9,
        reviewCount = 286,
        consultationFee = 85.0,
        consultationMode = ConsultationMode.BOTH,
        bio = "Specializing in preventive cardiology, hypertension, cardiac imaging, and arrhythmia management. Passionate about empowering patients with cardiovascular health.",
        nextAvailableSlot = "Today, 11:30 AM",
        verified = true,
        avatarColorHex = 0xFF0284C7
      ),
      Doctor(
        id = "doc-02",
        name = "Dr. Michael Chen",
        title = "MD, PhD - Consultant Neurologist",
        specialty = Specialty.NEUROLOGY,
        qualifications = "Johns Hopkins • 12 yrs exp",
        clinicId = "cl-02",
        clinicName = "Bayview Multispecialty Medical Center",
        experienceYears = 12,
        rating = 4.8,
        reviewCount = 194,
        consultationFee = 90.0,
        consultationMode = ConsultationMode.BOTH,
        bio = "Expert in migraine management, neuromuscular disorders, cognitive evaluation, and epilepsy care with a patient-centered approach.",
        nextAvailableSlot = "Tomorrow, 09:30 AM",
        verified = true,
        avatarColorHex = 0xFF6366F1
      ),
      Doctor(
        id = "doc-03",
        name = "Dr. Elena Rostova",
        title = "MD - Board Certified Dermatologist",
        specialty = Specialty.DERMATOLOGY,
        qualifications = "Stanford Medicine • 10 yrs exp",
        clinicId = "cl-04",
        clinicName = "Luminary Dermatology & Aesthetics",
        experienceYears = 10,
        rating = 4.9,
        reviewCount = 310,
        consultationFee = 75.0,
        consultationMode = ConsultationMode.IN_CLINIC,
        bio = "Focused on medical dermatology, dermatoscopy, psoriasis, acne, and proactive skin cancer surveillance using advanced diagnostic imaging.",
        nextAvailableSlot = "Today, 02:00 PM",
        verified = true,
        avatarColorHex = 0xFF0D9488
      ),
      Doctor(
        id = "doc-04",
        name = "Dr. David Kim",
        title = "MD, FAAP - Pediatric Specialist",
        specialty = Specialty.PEDIATRICS,
        qualifications = "UCSF Pediatric Residency • 8 yrs exp",
        clinicId = "cl-02",
        clinicName = "Bayview Multispecialty Medical Center",
        experienceYears = 8,
        rating = 4.9,
        reviewCount = 175,
        consultationFee = 60.0,
        consultationMode = ConsultationMode.BOTH,
        bio = "Warm and dedicated pediatrician committed to child development, immunization programs, allergy mitigation, and family guidance.",
        nextAvailableSlot = "Today, 04:15 PM",
        verified = true,
        avatarColorHex = 0xFFF59E0B
      ),
      Doctor(
        id = "doc-05",
        name = "Dr. Rachel Green",
        title = "MD - Orthopedic Surgeon",
        specialty = Specialty.ORTHOPEDICS,
        qualifications = "Columbia University • 15 yrs exp",
        clinicId = "cl-03",
        clinicName = "Apex Orthopedics & Sports Care",
        experienceYears = 15,
        rating = 4.7,
        reviewCount = 220,
        consultationFee = 95.0,
        consultationMode = ConsultationMode.IN_CLINIC,
        bio = "Specializing in sports injuries, knee and hip arthroscopy, tendon repair, and post-operative functional rehabilitation.",
        nextAvailableSlot = "Tomorrow, 11:00 AM",
        verified = true,
        avatarColorHex = 0xFF10B981
      ),
      Doctor(
        id = "doc-06",
        name = "Dr. Marcus Vance",
        title = "MD - Primary Care Physician",
        specialty = Specialty.GENERAL_MEDICINE,
        qualifications = "Mayo Clinic College of Medicine • 11 yrs exp",
        clinicId = "cl-02",
        clinicName = "Bayview Multispecialty Medical Center",
        experienceYears = 11,
        rating = 4.8,
        reviewCount = 340,
        consultationFee = 55.0,
        consultationMode = ConsultationMode.BOTH,
        bio = "Comprehensive primary healthcare, routine screenings, chronic condition management (diabetes, cholesterol), and health coaching.",
        nextAvailableSlot = "Today, 10:00 AM",
        verified = true,
        avatarColorHex = 0xFF0284C7
      )
    )
    _doctors.value = initialDoctors

    val todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val tomorrowStr = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val pastStr = LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val initialAppointments = listOf(
      Appointment(
        id = "apt-001",
        patientId = "pat-1001",
        patientName = "Alex Morgan",
        patientPhone = "+1 (555) 234-8901",
        patientAge = 32,
        doctorId = "doc-01",
        doctorName = "Dr. Sarah Jenkins",
        doctorSpecialty = "Cardiology",
        clinicName = "Nova Heart & Vascular Institute",
        clinicAddress = "742 Medical Center Blvd, Suite 400",
        date = todayStr,
        time = "10:30 AM",
        tokenNumber = 7,
        consultationMode = ConsultationMode.IN_CLINIC,
        visitReason = "Routine Cardiac Follow-up",
        symptomsNote = "Occasional palpitations after heavy exercise. Bring latest blood test reports.",
        status = AppointmentStatus.CONFIRMED,
        consultationFee = 85.0
      ),
      Appointment(
        id = "apt-002",
        patientId = "pat-1002",
        patientName = "Jessica Taylor",
        patientPhone = "+1 (555) 912-4411",
        patientAge = 45,
        doctorId = "doc-01",
        doctorName = "Dr. Sarah Jenkins",
        doctorSpecialty = "Cardiology",
        clinicName = "Nova Heart & Vascular Institute",
        clinicAddress = "742 Medical Center Blvd, Suite 400",
        date = todayStr,
        time = "11:30 AM",
        tokenNumber = 8,
        consultationMode = ConsultationMode.IN_CLINIC,
        visitReason = "Hypertension Review",
        symptomsNote = "Morning blood pressure averaging 138/88 mmHg. Discuss medication dosage.",
        status = AppointmentStatus.CHECKED_IN,
        consultationFee = 85.0
      ),
      Appointment(
        id = "apt-003",
        patientId = "pat-1003",
        patientName = "Robert Chen",
        patientPhone = "+1 (555) 345-6712",
        patientAge = 58,
        doctorId = "doc-01",
        doctorName = "Dr. Sarah Jenkins",
        doctorSpecialty = "Cardiology",
        clinicName = "Nova Heart & Vascular Institute",
        clinicAddress = "742 Medical Center Blvd, Suite 400",
        date = todayStr,
        time = "02:15 PM",
        tokenNumber = 12,
        consultationMode = ConsultationMode.VIDEO,
        visitReason = "Post-Procedure Consultation",
        symptomsNote = "Holter monitor follow-up discussion.",
        status = AppointmentStatus.CONFIRMED,
        consultationFee = 85.0
      ),
      Appointment(
        id = "apt-004",
        patientId = "pat-1001",
        patientName = "Alex Morgan",
        patientPhone = "+1 (555) 234-8901",
        patientAge = 32,
        doctorId = "doc-03",
        doctorName = "Dr. Elena Rostova",
        doctorSpecialty = "Dermatology",
        clinicName = "Luminary Dermatology & Aesthetics",
        clinicAddress = "88 Post Street, 6th Floor",
        date = tomorrowStr,
        time = "02:30 PM",
        tokenNumber = 14,
        consultationMode = ConsultationMode.IN_CLINIC,
        visitReason = "Annual Skin Mole Check",
        symptomsNote = "Noticed darker pigmentation on left forearm.",
        status = AppointmentStatus.CONFIRMED,
        consultationFee = 75.0
      ),
      Appointment(
        id = "apt-005",
        patientId = "pat-1001",
        patientName = "Alex Morgan",
        patientPhone = "+1 (555) 234-8901",
        patientAge = 32,
        doctorId = "doc-06",
        doctorName = "Dr. Marcus Vance",
        doctorSpecialty = "General Medicine",
        clinicName = "Bayview Multispecialty Medical Center",
        clinicAddress = "1200 Pacific Heights Ave",
        date = pastStr,
        time = "09:00 AM",
        tokenNumber = 3,
        consultationMode = ConsultationMode.IN_CLINIC,
        visitReason = "Annual Executive Physical",
        symptomsNote = "General wellness and lipid panel review.",
        status = AppointmentStatus.COMPLETED,
        consultationFee = 55.0,
        doctorNotes = "Vital signs optimal. Cholesterol within target limits. Recommended 30 mins moderate daily cardio.",
        prescriptionSummary = "Vitamin D3 2000 IU daily; Omega-3 Fish Oil 1000mg"
      )
    )
    _appointments.value = initialAppointments

    // Pre-mark booked slots
    val initialBooked = mutableSetOf<String>()
    initialAppointments.forEach { apt ->
      initialBooked.add("${apt.doctorId}_${apt.date}_${apt.time}")
    }
    bookedSlots.value = initialBooked
  }

  fun getSlotsForDoctorAndDate(doctorId: String, date: String): List<TimeSlot> {
    val times = listOf(
      "09:00 AM" to SlotPeriod.MORNING,
      "09:30 AM" to SlotPeriod.MORNING,
      "10:00 AM" to SlotPeriod.MORNING,
      "10:30 AM" to SlotPeriod.MORNING,
      "11:00 AM" to SlotPeriod.MORNING,
      "11:30 AM" to SlotPeriod.MORNING,
      "02:00 PM" to SlotPeriod.AFTERNOON,
      "02:30 PM" to SlotPeriod.AFTERNOON,
      "03:00 PM" to SlotPeriod.AFTERNOON,
      "03:30 PM" to SlotPeriod.AFTERNOON,
      "04:00 PM" to SlotPeriod.AFTERNOON,
      "05:00 PM" to SlotPeriod.EVENING,
      "05:30 PM" to SlotPeriod.EVENING,
      "06:00 PM" to SlotPeriod.EVENING
    )

    return times.mapIndexed { index, pair ->
      val timeStr = pair.first
      val period = pair.second
      val slotKey = "${doctorId}_${date}_$timeStr"
      val isBooked = bookedSlots.value.contains(slotKey)
      val isBlocked = blockedSlots.value.contains(slotKey)

      TimeSlot(
        id = "slot_${doctorId}_${date}_$index",
        doctorId = doctorId,
        date = date,
        time = timeStr,
        period = period,
        tokenNumber = index + 1,
        isBooked = isBooked,
        isBlocked = isBlocked
      )
    }
  }

  @Synchronized
  fun bookAppointment(
    patientId: String,
    patientName: String,
    patientPhone: String,
    doctor: Doctor,
    date: String,
    time: String,
    tokenNumber: Int,
    mode: ConsultationMode,
    reason: String,
    notes: String
  ): Result<Appointment> {
    val slotKey = "${doctor.id}_${date}_$time"
    if (bookedSlots.value.contains(slotKey) || blockedSlots.value.contains(slotKey)) {
      return Result.failure(IllegalStateException("Selected slot $time on $date is no longer available."))
    }

    val newAppointment = Appointment(
      id = "apt-${UUID.randomUUID().toString().take(8)}",
      patientId = patientId,
      patientName = patientName,
      patientPhone = patientPhone,
      doctorId = doctor.id,
      doctorName = doctor.name,
      doctorSpecialty = doctor.specialty.displayName,
      clinicName = doctor.clinicName,
      clinicAddress = clinics.value.firstOrNull { it.id == doctor.clinicId }?.address ?: "Medical Suite",
      date = date,
      time = time,
      tokenNumber = tokenNumber,
      consultationMode = mode,
      visitReason = reason,
      symptomsNote = notes,
      status = AppointmentStatus.CONFIRMED,
      consultationFee = doctor.consultationFee,
      createdAtMillis = System.currentTimeMillis()
    )

    // Atomic update
    val updatedBooked = bookedSlots.value.toMutableSet()
    updatedBooked.add(slotKey)
    bookedSlots.value = updatedBooked

    _appointments.value = listOf(newAppointment) + _appointments.value
    return Result.success(newAppointment)
  }

  fun updateAppointmentStatus(
    appointmentId: String,
    newStatus: AppointmentStatus,
    notes: String = "",
    prescriptions: String = ""
  ) {
    _appointments.value = _appointments.value.map { apt ->
      if (apt.id == appointmentId) {
        apt.copy(
          status = newStatus,
          doctorNotes = if (notes.isNotEmpty()) notes else apt.doctorNotes,
          prescriptionSummary = if (prescriptions.isNotEmpty()) prescriptions else apt.prescriptionSummary
        )
      } else {
        apt
      }
    }
  }

  fun cancelAppointment(appointmentId: String, reason: String) {
    val target = _appointments.value.firstOrNull { it.id == appointmentId } ?: return
    val slotKey = "${target.doctorId}_${target.date}_${target.time}"

    // Release slot
    val updatedBooked = bookedSlots.value.toMutableSet()
    updatedBooked.remove(slotKey)
    bookedSlots.value = updatedBooked

    _appointments.value = _appointments.value.map {
      if (it.id == appointmentId) {
        it.copy(status = AppointmentStatus.CANCELLED, doctorNotes = "Cancelled by patient: $reason")
      } else {
        it
      }
    }
  }

  fun rescheduleAppointment(
    appointmentId: String,
    newDate: String,
    newTime: String,
    newToken: Int
  ): Boolean {
    val target = _appointments.value.firstOrNull { it.id == appointmentId } ?: return false
    val oldSlotKey = "${target.doctorId}_${target.date}_${target.time}"
    val newSlotKey = "${target.doctorId}_${newDate}_$newTime"

    if (bookedSlots.value.contains(newSlotKey) || blockedSlots.value.contains(newSlotKey)) {
      return false
    }

    val updatedBooked = bookedSlots.value.toMutableSet()
    updatedBooked.remove(oldSlotKey)
    updatedBooked.add(newSlotKey)
    bookedSlots.value = updatedBooked

    _appointments.value = _appointments.value.map {
      if (it.id == appointmentId) {
        it.copy(date = newDate, time = newTime, tokenNumber = newToken, status = AppointmentStatus.CONFIRMED)
      } else {
        it
      }
    }
    return true
  }

  fun toggleBlockSlot(doctorId: String, date: String, time: String) {
    val key = "${doctorId}_${date}_$time"
    val updated = blockedSlots.value.toMutableSet()
    if (updated.contains(key)) {
      updated.remove(key)
    } else {
      updated.add(key)
    }
    blockedSlots.value = updated
  }

  fun updateDoctorStatus(status: DoctorStatus) {
    _doctorProfile.value = _doctorProfile.value.copy(status = status)
  }

  fun updatePatientCity(city: String) {
    _patientProfile.value = _patientProfile.value.copy(selectedCity = city)
  }

  fun updatePatientProfile(updated: PatientProfile) {
    _patientProfile.value = updated
  }

  fun loginPatient(phone: String, name: String = "Alex Morgan") {
    _patientSession.value = AuthSession(
      isAuthenticated = true,
      role = UserRole.PATIENT,
      userId = "pat-${phone.takeLast(4).ifEmpty { "1001" }}",
      userName = name,
      token = "jwt_pat_${System.currentTimeMillis()}",
      emailOrPhone = phone
    )
  }

  fun logoutPatient() {
    _patientSession.value = AuthSession(isAuthenticated = false, role = UserRole.PATIENT)
  }

  fun loginDoctor(licenseNumber: String) {
    val matchedDoc = doctors.value.firstOrNull { it.id == "doc-01" }
    _doctorSession.value = AuthSession(
      isAuthenticated = true,
      role = UserRole.DOCTOR,
      userId = matchedDoc?.id ?: "doc-01",
      userName = matchedDoc?.name ?: "Dr. Sarah Jenkins",
      token = "jwt_doc_${System.currentTimeMillis()}",
      emailOrPhone = licenseNumber
    )
  }

  fun logoutDoctor() {
    _doctorSession.value = AuthSession(isAuthenticated = false, role = UserRole.DOCTOR)
  }

  companion object {
    val instance: HealthcareRepository by lazy { HealthcareRepository() }
  }
}
