package com.example.ui.doctor

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.common.AppointmentStatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorConsultationSheet(
  appointment: Appointment,
  repository: HealthcareRepository,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  var clinicalNotes by remember { mutableStateOf(appointment.doctorNotes) }
  var prescriptions by remember { mutableStateOf(appointment.prescriptionSummary) }
  var isSaved by remember { mutableStateOf(false) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    containerColor = MaterialTheme.colorScheme.surface,
    modifier = modifier.testTag("doctor_consultation_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp)
        .padding(bottom = 30.dp)
        .navigationBarsPadding()
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Patient Consultation",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Token #${appointment.tokenNumber} • ${appointment.time}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
          )
        }
        AppointmentStatusBadge(status = appointment.status)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Patient Identity (Authorized role information only)
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Authorized Patient Record",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = appointment.patientName,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
              )
              Text(
                text = "Age: ${appointment.patientAge} • ${appointment.patientGender} • Phone: ${appointment.patientPhone}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = appointment.consultationMode.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          HorizontalDivider()
          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Visit Reason: ${appointment.visitReason}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )

          if (appointment.symptomsNote.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Patient Stated Symptoms: “${appointment.symptomsNote}”",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Workflow Status Transitions
      Text(
        text = "Consultation Status Workflow",
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (appointment.status == AppointmentStatus.CONFIRMED) {
          Button(
            onClick = {
              repository.updateAppointmentStatus(appointment.id, AppointmentStatus.CHECKED_IN)
              onDismiss()
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StatusCheckedIn),
            modifier = Modifier
              .weight(1f)
              .testTag("btn_mark_checked_in")
          ) {
            Icon(Icons.Default.CheckCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Check-In", fontSize = 12.sp)
          }
        }

        if (appointment.status == AppointmentStatus.CONFIRMED || appointment.status == AppointmentStatus.CHECKED_IN) {
          Button(
            onClick = {
              repository.updateAppointmentStatus(appointment.id, AppointmentStatus.IN_CONSULTATION)
              onDismiss()
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = StatusInConsultation),
            modifier = Modifier
              .weight(1f)
              .testTag("btn_begin_consultation")
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Begin Consult", fontSize = 12.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Doctor Clinical Notes & Prescription Area
      Text(
        text = "Clinical Notes & Prescription Advice",
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = clinicalNotes,
        onValueChange = { clinicalNotes = it },
        label = { Text("Clinical Assessment / Diagnosis Notes") },
        placeholder = { Text("E.g. Normal S1/S2 heart sounds, BP 124/80. Follow up in 3 months.") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_doctor_notes"),
        minLines = 3,
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = prescriptions,
        onValueChange = { prescriptions = it },
        label = { Text("Prescription / Recommended Medications") },
        placeholder = { Text("E.g. Amlodipine 5mg once daily, CoQ10 100mg") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_doctor_prescription"),
        minLines = 2,
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Complete Consultation Button
      Button(
        onClick = {
          repository.updateAppointmentStatus(
            appointmentId = appointment.id,
            newStatus = AppointmentStatus.COMPLETED,
            notes = clinicalNotes.ifBlank { "Consultation conducted and patient advice provided." },
            prescriptions = prescriptions
          )
          isSaved = true
          onDismiss()
        },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = StatusCompleted),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("btn_complete_consultation")
      ) {
        Icon(Icons.Default.TaskAlt, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (appointment.status == AppointmentStatus.COMPLETED) "Update Clinical Record" else "Complete Consultation & Sign",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Cancel appointment action
      if (appointment.status != AppointmentStatus.COMPLETED && appointment.status != AppointmentStatus.CANCELLED) {
        TextButton(
          onClick = {
            repository.updateAppointmentStatus(appointment.id, AppointmentStatus.CANCELLED, notes = "Cancelled by clinic.")
            onDismiss()
          },
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Cancel or Reschedule Visit", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }
      }
    }
  }
}
