package com.example.ui.doctor

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DoctorScheduleScreen(
  doctorProfile: DoctorProfile,
  appointments: List<Appointment>,
  repository: HealthcareRepository,
  onSelectAppointment: (Appointment) -> Unit,
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

  // Load slots for selected date
  val slots = remember(selectedDate, doctorProfile.id, appointments) {
    repository.getSlotsForDoctorAndDate(doctorProfile.id, selectedDate)
  }

  val dateAppointments = remember(selectedDate, appointments, doctorProfile.id) {
    appointments.filter { it.doctorId == doctorProfile.id && it.date == selectedDate }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Clinic Schedule & Slots",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = "Assigned clinic working hours, capacity management, and slot reservation controls",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Clinic Schedule Header Banner
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = doctorProfile.clinicAffiliation,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
            Text(
              text = "Working Hours: ${doctorProfile.todayWorkingHours} (20 min intervals)",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(ClinicalTealLight)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "ACTIVE",
              color = ClinicalTealDark,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Date Switcher Row
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(dateOptions) { (dateStr, label) ->
          val isSelected = selectedDate == dateStr
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(if (isSelected) DoctorNavyPrimary else MaterialTheme.colorScheme.surfaceVariant)
              .clickable { selectedDate = dateStr }
              .padding(horizontal = 14.dp, vertical = 10.dp)
              .testTag("doc_schedule_date_$dateStr"),
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

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Time Slots & Bookings (${slots.size})",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(8.dp))
    }

    items(slots, key = { it.id }) { slot ->
      val bookedApt = dateAppointments.firstOrNull { it.time == slot.time }
      val isBlocked = slot.isBlocked

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 10.dp)
          .clip(RoundedCornerShape(14.dp))
          .clickable(enabled = bookedApt != null) {
            bookedApt?.let { onSelectAppointment(it) }
          }
          .testTag("slot_row_${slot.time.replace(" ", "_")}"),
        colors = CardDefaults.cardColors(
          containerColor = when {
            isBlocked -> Color(0xFFFEE2E2).copy(alpha = 0.6f)
            bookedApt != null -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (bookedApt != null) 2.dp else 0.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (bookedApt != null) MedicalBluePrimary else Color.LightGray.copy(alpha = 0.3f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "#${slot.tokenNumber}",
                color = if (bookedApt != null) Color.White else Color.DarkGray,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Text(
                text = slot.time,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = when {
                  isBlocked -> "Blocked by Doctor (Surgery / Rounds)"
                  bookedApt != null -> "${bookedApt.patientName} (${bookedApt.status.label})"
                  else -> "Available for Patient Booking"
                },
                fontSize = 12.sp,
                color = when {
                  isBlocked -> Color(0xFFDC2626)
                  bookedApt != null -> MaterialTheme.colorScheme.primary
                  else -> Color.Gray
                }
              )
            }
          }

          // Action button: If not booked, can Block/Unblock
          if (bookedApt == null) {
            TextButton(
              onClick = {
                repository.toggleBlockSlot(doctorProfile.id, selectedDate, slot.time)
              },
              modifier = Modifier.testTag("btn_block_slot_${slot.time.replace(" ", "_")}")
            ) {
              Text(
                text = if (isBlocked) "Unblock" else "Block Slot",
                fontSize = 12.sp,
                color = if (isBlocked) ClinicalTeal else Color(0xFFEF4444)
              )
            }
          } else {
            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = "View",
              tint = Color.Gray,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}
