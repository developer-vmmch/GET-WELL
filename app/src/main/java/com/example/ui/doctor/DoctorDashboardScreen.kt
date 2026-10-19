package com.example.ui.doctor

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HealthcareRepository
import com.example.model.*
import com.example.ui.common.AppointmentStatusBadge
import com.example.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DoctorDashboardScreen(
  doctorProfile: DoctorProfile,
  appointments: List<Appointment>,
  repository: HealthcareRepository,
  onSelectAppointment: (Appointment) -> Unit,
  onNavigateToSchedule: () -> Unit,
  modifier: Modifier = Modifier
) {
  val todayStr = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }

  val todayAppointments = remember(appointments, doctorProfile.id, todayStr) {
    appointments.filter { it.doctorId == doctorProfile.id && it.date == todayStr }
  }

  val totalToday = todayAppointments.size
  val completedToday = todayAppointments.count { it.status == AppointmentStatus.COMPLETED }
  val queueWaiting = todayAppointments.count {
    it.status == AppointmentStatus.CONFIRMED || it.status == AppointmentStatus.CHECKED_IN
  }
  val inConsultation = todayAppointments.count { it.status == AppointmentStatus.IN_CONSULTATION }

  val nextPatient = remember(todayAppointments) {
    todayAppointments.firstOrNull {
      it.status == AppointmentStatus.CHECKED_IN || it.status == AppointmentStatus.CONFIRMED
    }
  }

  var showStatusMenu by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // 1. Doctor Header with Availability Toggle
    item {
      Spacer(modifier = Modifier.height(14.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = doctorProfile.fullName,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${doctorProfile.title} • ${doctorProfile.roomNumber}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }

        // Status Dropdown Button
        Box {
          Row(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(
                when (doctorProfile.status) {
                  DoctorStatus.AVAILABLE -> Color(0xFFD1FAE5)
                  DoctorStatus.IN_CONSULTATION -> Color(0xFFFEF3C7)
                  DoctorStatus.ON_BREAK -> Color(0xFFE0F2FE)
                  DoctorStatus.OFF_DUTY -> Color(0xFFF3F4F6)
                }
              )
              .clickable { showStatusMenu = true }
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("doctor_status_toggle"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(
                  when (doctorProfile.status) {
                    DoctorStatus.AVAILABLE -> StatusCompleted
                    DoctorStatus.IN_CONSULTATION -> StatusInConsultation
                    DoctorStatus.ON_BREAK -> StatusConfirmed
                    DoctorStatus.OFF_DUTY -> Color.Gray
                  }
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = doctorProfile.status.label,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = when (doctorProfile.status) {
                DoctorStatus.AVAILABLE -> Color(0xFF065F46)
                DoctorStatus.IN_CONSULTATION -> Color(0xFF92400E)
                DoctorStatus.ON_BREAK -> MedicalBlueDark
                DoctorStatus.OFF_DUTY -> Color(0xFF374151)
              }
            )
            Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          }

          DropdownMenu(
            expanded = showStatusMenu,
            onDismissRequest = { showStatusMenu = false }
          ) {
            DoctorStatus.entries.forEach { status ->
              DropdownMenuItem(
                text = { Text(status.label, fontWeight = FontWeight.Medium) },
                onClick = {
                  repository.updateDoctorStatus(status)
                  showStatusMenu = false
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))
    }

    // 2. Metrics KPI Grid
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        DoctorStatCard(
          label = "Today's Total",
          value = totalToday.toString(),
          icon = Icons.Default.Today,
          color = MedicalBluePrimary,
          modifier = Modifier.weight(1f)
        )
        DoctorStatCard(
          label = "In Queue",
          value = queueWaiting.toString(),
          icon = Icons.Default.Queue,
          color = StatusInConsultation,
          modifier = Modifier.weight(1f)
        )
        DoctorStatCard(
          label = "Completed",
          value = completedToday.toString(),
          icon = Icons.Default.CheckCircle,
          color = StatusCompleted,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // 3. Next Patient Up Spotlight Card
    item {
      nextPatient?.let { patientApt ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
              1.5.dp,
              DoctorNavyPrimary.copy(alpha = 0.2f),
              RoundedCornerShape(20.dp)
            )
            .testTag("next_patient_card"),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(ClinicalTeal)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "NEXT PATIENT IN QUEUE",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 1.sp,
                  color = ClinicalTeal
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(DoctorNavyPrimary)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "Token #${patientApt.tokenNumber}",
                  color = DoctorAccentSky,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = patientApt.patientName,
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "${patientApt.patientAge} yrs • ${patientApt.patientGender} • ${patientApt.consultationMode.label}",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (patientApt.symptomsNote.isNotEmpty()) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Symptoms: “${patientApt.symptomsNote}”",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
              )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.AccessTime,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = patientApt.time,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }

              Button(
                onClick = { onSelectAppointment(patientApt) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DoctorNavyPrimary),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_start_next_consultation")
              ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Start Consult", fontWeight = FontWeight.Bold, fontSize = 12.sp)
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(24.dp))
      }
    }

    // 4. Today's Patient Queue Section
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Today's Patient Schedule (${todayAppointments.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )

        TextButton(onClick = onNavigateToSchedule) {
          Text("Full Schedule", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
    }

    if (todayAppointments.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.EventAvailable, contentDescription = null, tint = ClinicalTeal, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("No appointments scheduled for today", fontWeight = FontWeight.Bold)
            Text("Assigned clinic slots are ready for patient booking.", fontSize = 11.sp, color = Color.Gray)
          }
        }
      }
    } else {
      items(todayAppointments, key = { it.id }) { apt ->
        DoctorQueueItemCard(
          appointment = apt,
          onClick = { onSelectAppointment(apt) },
          modifier = Modifier.padding(bottom = 10.dp)
        )
      }
    }
  }
}

@Composable
private fun DoctorStatCard(
  label: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.clip(RoundedCornerShape(14.dp)),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = label,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun DoctorQueueItemCard(
  appointment: Appointment,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("doctor_queue_item_${appointment.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Token Circle
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(MedicalBlueLight),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "#${appointment.tokenNumber}",
          fontWeight = FontWeight.ExtraBold,
          fontSize = 14.sp,
          color = MedicalBlueDark
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = appointment.patientName,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          AppointmentStatusBadge(status = appointment.status)
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "${appointment.time} • ${appointment.visitReason}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "Manage",
        tint = Color.Gray,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
