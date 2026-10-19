package com.example.data

/**
 * CSVS IT Tech — Get Well Soon API Contract Specification v1.0
 *
 * Base URL: https://api.amazetechclans.com/getwellsoon/v1
 * Authentication: Bearer <JWT_TOKEN>
 *
 * 1. PATIENT AUTHENTICATION:
 *    - POST /auth/patient/send-otp
 *      Request: { "phone": "+15552348901" }
 *      Response: { "status": "OK", "expiresIn": 300, "devCode": "123456" }
 *    - POST /auth/patient/verify-otp
 *      Request: { "phone": "+15552348901", "otp": "123456" }
 *      Response: { "token": "jwt_pat_xxx", "patient": { ... } }
 *    - POST /auth/patient/register
 *      Request: { "fullName": "Alex Morgan", "phone": "...", "email": "...", "dateOfBirth": "..." }
 *
 * 2. DOCTOR AUTHENTICATION:
 *    - POST /auth/doctor/login
 *      Request: { "licenseNumber": "MED-US-89421", "password": "...", "clinicId": "cl-01" }
 *      Response: { "token": "jwt_doc_xxx", "doctor": { ... } }
 *
 * 3. CLINIC & DOCTOR DISCOVERY:
 *    - GET /clinics?city=San+Francisco&specialty=Cardiology
 *    - GET /doctors?clinicId=cl-01&specialty=Cardiology&mode=IN_CLINIC
 *    - GET /doctors/{id}/availability?date=2026-10-20
 *      Response: { "doctorId": "...", "date": "...", "slots": [ ... ] }
 *
 * 4. APPOINTMENTS (PATIENT & DOCTOR SHARED ENGINE):
 *    - POST /appointments/book
 *      Request: { "doctorId": "...", "date": "...", "timeSlotId": "...", "reason": "...", "mode": "IN_CLINIC" }
 *      Response: { "appointmentId": "...", "tokenNumber": 14, "status": "CONFIRMED" }
 *    - GET /patient/appointments
 *    - PATCH /appointments/{id}/reschedule
 *    - PATCH /appointments/{id}/cancel
 *      Request: { "reason": "Conflict with work schedule" }
 *
 * 5. DOCTOR SCHEDULE & WORKFLOW:
 *    - GET /doctor/schedule?date=2026-10-20
 *    - PATCH /doctor/appointments/{id}/status
 *      Request: { "newStatus": "CHECKED_IN" | "IN_CONSULTATION" | "COMPLETED", "doctorNotes": "...", "prescriptions": "..." }
 *    - PUT /doctor/slots/block
 *      Request: { "slotId": "...", "isBlocked": true, "reason": "Emergency surgery" }
 */
object ApiEndpoints {
  const val BASE_URL = "https://api.amazetechclans.com/getwellsoon/v1"
  const val PATIENT_LOGIN_OTP = "/auth/patient/send-otp"
  const val PATIENT_VERIFY_OTP = "/auth/patient/verify-otp"
  const val DOCTOR_LOGIN = "/auth/doctor/login"
  const val GET_CLINICS = "/clinics"
  const val GET_DOCTORS = "/doctors"
  const val GET_SLOTS = "/doctors/{id}/availability"
  const val BOOK_APPOINTMENT = "/appointments/book"
  const val GET_PATIENT_APPOINTMENTS = "/patient/appointments"
  const val UPDATE_APPOINTMENT_STATUS = "/appointments/{id}/status"
}
