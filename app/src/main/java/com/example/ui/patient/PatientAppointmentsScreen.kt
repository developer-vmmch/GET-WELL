package com.example.ui.patient

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HealthcareRepository
import com.example.model.*
import com.example.ui.common.AppointmentStatusBadge
import com.example.ui.common.QrPassCard
import com.example.ui.theme.*

@Composable
fun PatientAppointmentsScreen(
  appointments: List<Appointment>,
  repository: HealthcareRepository,
  onViewQrPass: (Appointment) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Upcoming, 1: Completed, 2: Cancelled
  var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }
  var cancelReason by remember { mutableStateOf("") }
  var viewingPassAppointment by remember { mutableStateOf<Appointment?>(null) }

  // Filter appointments for current patient
  val filteredList = remember(appointments, selectedTab) {
    when (selectedTab) {
      0 -> appointments.filter {
        it.status == AppointmentStatus.CONFIRMED ||
          it.status == AppointmentStatus.CHECKED_IN ||
          it.status == AppointmentStatus.IN_CONSULTATION ||
          it.status == AppointmentStatus.PENDING
      }
      1 -> appointments.filter { it.status == AppointmentStatus.COMPLETED }
      else -> appointments.filter { it.status == AppointmentStatus.CANCELLED }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = "My Appointments",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )

    Text(
      text = "Manage your scheduled visits, passes, and doctor consultation notes",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Filter Tabs
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
      modifier = Modifier.clip(RoundedCornerShape(12.dp))
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Text(
            "Upcoming",
            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
          )
        },
        modifier = Modifier.testTag("tab_upcoming_appointments")
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Text(
            "Completed",
            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
          )
        },
        modifier = Modifier.testTag("tab_completed_appointments")
      )
      Tab(
        selected = selectedTab == 2,
        onClick = { selectedTab = 2 },
        text = {
          Text(
            "Cancelled",
            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
          )
        },
        modifier = Modifier.testTag("tab_cancelled_appointments")
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    if (filteredList.isEmpty()) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(
          imageVector = Icons.Default.EventBusy,
          contentDescription = null,
          tint = Color.Gray,
          modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = when (selectedTab) {
            0 -> "No upcoming appointments"
            1 -> "No completed visits yet"
            else -> "No cancelled appointments"
          },
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Find doctors and book your next clinic visit from Discover.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
      ) {
        items(filteredList, key = { it.id }) { apt ->
          PatientAppointmentCard(
            appointment = apt,
            onViewPass = { viewingPassAppointment = apt },
            onCancel = { appointmentToCancel = apt },
            modifier = Modifier.padding(bottom = 12.dp)
          )
        }
      }
    }
  }

  // Cancel Appointment Confirmation Dialog
  appointmentToCancel?.let { apt ->
    AlertDialog(
      onDismissRequest = { appointmentToCancel = null },
      title = { Text("Cancel Appointment", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text(
            "Are you sure you want to cancel your visit with ${apt.doctorName} on ${apt.date} at ${apt.time}?",
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = cancelReason,
            onValueChange = { cancelReason = it },
            placeholder = { Text("Reason for cancellation...") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_cancel_reason"),
            shape = RoundedCornerShape(10.dp)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            repository.cancelAppointment(apt.id, cancelReason.ifBlank { "Personal reason" })
            appointmentToCancel = null
            cancelReason = ""
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
          modifier = Modifier.testTag("btn_confirm_cancel_appointment")
        ) {
          Text("Cancel Visit")
        }
      },
      dismissButton = {
        TextButton(onClick = { appointmentToCancel = null }) {
          Text("Keep Appointment")
        }
      }
    )
  }

  // Pass Modal Dialog
  viewingPassAppointment?.let { apt ->
    AlertDialog(
      onDismissRequest = { viewingPassAppointment = null },
      confirmButton = {},
      text = {
        QrPassCard(
          appointment = apt,
          onDismiss = { viewingPassAppointment = null }
        )
      }
    )
  }
}

@Composable
private fun PatientAppointmentCard(
  appointment: Appointment,
  onViewPass: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .testTag("patient_apt_card_${appointment.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(MedicalBlueLight)
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "Token #${appointment.tokenNumber}",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MedicalBlueDark
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = appointment.consultationMode.label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        AppointmentStatusBadge(status = appointment.status)
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = appointment.doctorName,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = "${appointment.doctorSpecialty} • ${appointment.clinicName}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(6.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.CalendarToday,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "${appointment.date} at ${appointment.time}",
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = appointment.clinicAddress,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      if (appointment.status == AppointmentStatus.COMPLETED && appointment.doctorNotes.isNotEmpty()) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp)
        ) {
          Column {
            Text(
              text = "Doctor Consultation Summary:",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = appointment.doctorNotes,
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurface
            )
            if (appointment.prescriptionSummary.isNotEmpty()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Prescription: ${appointment.prescriptionSummary}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = ClinicalTealDark
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (appointment.status == AppointmentStatus.CONFIRMED || appointment.status == AppointmentStatus.CHECKED_IN) {
          OutlinedButton(
            onClick = onCancel,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(32.dp)
          ) {
            Text("Cancel", fontSize = 11.sp, color = MaterialTheme.colorScheme.error)
          }

          Spacer(modifier = Modifier.width(8.dp))

          Button(
            onClick = onViewPass,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MedicalBluePrimary),
            modifier = Modifier.height(32.dp)
          ) {
            Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Digital Pass", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        } else if (appointment.status == AppointmentStatus.COMPLETED) {
          Button(
            onClick = onViewPass,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ClinicalTeal),
            modifier = Modifier.height(32.dp)
          ) {
            Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("View Record", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
