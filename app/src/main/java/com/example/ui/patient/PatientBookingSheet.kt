package com.example.ui.patient

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HealthcareRepository
import com.example.model.*
import com.example.ui.common.QrPassCard
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PatientBookingSheet(
  doctor: Doctor,
  patientProfile: PatientProfile,
  repository: HealthcareRepository,
  onDismiss: () -> Unit,
  onBookingComplete: (Appointment) -> Unit,
  modifier: Modifier = Modifier
) {
  val dateOptions = remember {
    (0..6).map { offset ->
      val date = LocalDate.now().plusDays(offset.toLong())
      val dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      val label = when (offset) {
        0 -> "Today"
        1 -> "Tomorrow"
        else -> date.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
      }
      dateStr to label
    }
  }

  var selectedDate by remember { mutableStateOf(dateOptions.first().first) }
  var selectedSlot by remember { mutableStateOf<TimeSlot?>(null) }
  var selectedMode by remember { mutableStateOf(ConsultationMode.IN_CLINIC) }
  var visitReason by remember { mutableStateOf("Routine Checkup") }
  var symptomsNote by remember { mutableStateOf("") }

  var bookingError by remember { mutableStateOf<String?>(null) }
  var confirmedAppointment by remember { mutableStateOf<Appointment?>(null) }
  var isSubmitting by remember { mutableStateOf(false) }

  // Load available slots dynamically for doctor + selected date
  val availableSlots = remember(selectedDate, doctor.id) {
    repository.getSlotsForDoctorAndDate(doctor.id, selectedDate)
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier.testTag("booking_bottom_sheet")
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .navigationBarsPadding()
    ) {
      if (confirmedAppointment != null) {
        // Success View with Digital Pass
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(ClinicalTealLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Success",
              tint = ClinicalTeal,
              modifier = Modifier.size(40.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "Appointment Confirmed!",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = "Your slot and queue token have been reserved.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          QrPassCard(
            appointment = confirmedAppointment!!,
            onDismiss = {
              onBookingComplete(confirmedAppointment!!)
              onDismiss()
            }
          )

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = {
              onBookingComplete(confirmedAppointment!!)
              onDismiss()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("btn_done_booking"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MedicalBluePrimary)
          ) {
            Text("View in My Appointments", fontWeight = FontWeight.Bold)
          }
        }
      } else {
        // Step-by-Step Booking Form
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 30.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Book Appointment",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "${doctor.name} • ${doctor.specialty.displayName}",
                style = MaterialTheme.typography.bodySmall,
                color = MedicalBluePrimary
              )
            }
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // 1. Consultation Mode (In-Clinic or Video)
          Text(
            text = "1. Consultation Mode",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            listOf(
              ConsultationMode.IN_CLINIC to Icons.Default.LocalHospital,
              ConsultationMode.VIDEO to Icons.Default.VideoCall
            ).forEach { (mode, icon) ->
              val isSelected = selectedMode == mode
              Card(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .border(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) MedicalBluePrimary else MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(12.dp)
                  )
                  .clickable { selectedMode = mode }
                  .testTag("mode_${mode.name}"),
                colors = CardDefaults.cardColors(
                  containerColor = if (isSelected) MedicalBlueLight else MaterialTheme.colorScheme.surface
                )
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) MedicalBlueDark else Color.Gray,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = mode.label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MedicalBlueDark else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // 2. Select Date
          Text(
            text = "2. Select Date",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))

          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(dateOptions) { (dateStr, label) ->
              val isSelected = selectedDate == dateStr
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isSelected) MedicalBluePrimary else MaterialTheme.colorScheme.surfaceVariant)
                  .clickable {
                    selectedDate = dateStr
                    selectedSlot = null
                  }
                  .padding(horizontal = 14.dp, vertical = 10.dp)
                  .testTag("date_chip_$dateStr"),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = label,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // 3. Choose Time Slot (Token-based)
          Text(
            text = "3. Available Time & Token Slot",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))

          // Group by Period
          listOf(SlotPeriod.MORNING, SlotPeriod.AFTERNOON, SlotPeriod.EVENING).forEach { period ->
            val slotsInPeriod = availableSlots.filter { it.period == period }
            if (slotsInPeriod.isNotEmpty()) {
              Text(
                text = period.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 4.dp)
              )

              FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                slotsInPeriod.forEach { slot ->
                  val isSelected = selectedSlot?.id == slot.id
                  val isAvailable = !slot.isBooked && !slot.isBlocked

                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(10.dp))
                      .background(
                        when {
                          !isAvailable -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                          isSelected -> MedicalBluePrimary
                          else -> MaterialTheme.colorScheme.surface
                        }
                      )
                      .border(
                        1.dp,
                        when {
                          !isAvailable -> Color.Transparent
                          isSelected -> MedicalBluePrimary
                          else -> MaterialTheme.colorScheme.outline
                        },
                        RoundedCornerShape(10.dp)
                      )
                      .clickable(enabled = isAvailable) {
                        selectedSlot = slot
                        bookingError = null
                      }
                      .padding(horizontal = 10.dp, vertical = 8.dp)
                      .testTag("slot_${slot.time.replace(" ", "_")}"),
                    contentAlignment = Alignment.Center
                  ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                      Text(
                        text = slot.time,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                          !isAvailable -> Color.Gray
                          isSelected -> Color.White
                          else -> MaterialTheme.colorScheme.onSurface
                        }
                      )
                      Text(
                        text = if (!isAvailable) "Booked" else "Token #${slot.tokenNumber}",
                        fontSize = 9.sp,
                        color = when {
                          !isAvailable -> Color.LightGray
                          isSelected -> Color.White.copy(alpha = 0.8f)
                          else -> ClinicalTeal
                        }
                      )
                    }
                  }
                }
              }
              Spacer(modifier = Modifier.height(10.dp))
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 4. Visit Reason & Note
          Text(
            text = "4. Reason for Visit",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))

          val reasons = listOf("Routine Checkup", "Follow-up Visit", "Acute Symptoms", "Prescription Renewal")
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(reasons) { reason ->
              FilterChip(
                selected = visitReason == reason,
                onClick = { visitReason = reason },
                label = { Text(reason, fontSize = 11.sp) },
                shape = RoundedCornerShape(8.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = symptomsNote,
            onValueChange = { symptomsNote = it },
            placeholder = { Text("Briefly describe any current symptoms (optional)...") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_booking_notes"),
            maxLines = 3,
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Summary & Fee Card
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(14.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Consultation Fee", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$${doctor.consultationFee.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Clinic", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(doctor.clinicName, fontSize = 12.sp, fontWeight = FontWeight.Medium)
              }
              if (selectedSlot != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("Reserved Token", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                  Text("Token #${selectedSlot!!.tokenNumber} (${selectedSlot!!.time})", fontWeight = FontWeight.Bold, color = ClinicalTeal)
                }
              }
            }
          }

          bookingError?.let { err ->
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = err,
              color = MaterialTheme.colorScheme.error,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Confirm CTA
          Button(
            onClick = {
              if (selectedSlot == null) {
                bookingError = "Please select an available time slot."
                return@Button
              }
              isSubmitting = true
              val result = repository.bookAppointment(
                patientId = patientProfile.id,
                patientName = patientProfile.fullName,
                patientPhone = patientProfile.phone,
                doctor = doctor,
                date = selectedDate,
                time = selectedSlot!!.time,
                tokenNumber = selectedSlot!!.tokenNumber,
                mode = selectedMode,
                reason = visitReason,
                notes = symptomsNote
              )

              isSubmitting = false
              result.onSuccess { newApt ->
                confirmedAppointment = newApt
              }.onFailure { ex ->
                bookingError = ex.message ?: "Could not complete booking. Please try another slot."
              }
            },
            enabled = selectedSlot != null && !isSubmitting,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("btn_confirm_appointment"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MedicalBluePrimary)
          ) {
            if (isSubmitting) {
              CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            } else {
              Text("Confirm Booking & Generate Pass", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
          }
        }
      }
    }
  }
}
