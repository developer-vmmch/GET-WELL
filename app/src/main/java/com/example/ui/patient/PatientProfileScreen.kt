package com.example.ui.patient

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.model.PatientProfile
import com.example.ui.theme.*

@Composable
fun PatientProfileScreen(
  patientProfile: PatientProfile,
  repository: HealthcareRepository,
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  onSwitchToDoctorApp: () -> Unit,
  onSignOut: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showEditDialog by remember { mutableStateOf(false) }
  var smsReminders by remember { mutableStateOf(true) }
  var queueAlerts by remember { mutableStateOf(true) }
  var healthTips by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // Profile Avatar
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(MedicalBluePrimary),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = patientProfile.fullName.split(" ").mapNotNull { it.firstOrNull() }.joinToString("").take(2),
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = patientProfile.fullName,
      style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )

    Text(
      text = "${patientProfile.phone} • ${patientProfile.email}",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Health Stats Row: Blood Group, DOB, Allergies
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("Blood Group", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(patientProfile.bloodGroup, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MedicalBluePrimary)
        }
      }

      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("Gender", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(patientProfile.gender, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
      }

      Card(
        modifier = Modifier.weight(1.2f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(
          modifier = Modifier.padding(12.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("Emergency", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(patientProfile.emergencyContact.split(" ").firstOrNull() ?: "Family", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Profile Actions Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Account & Medical Profile", fontWeight = FontWeight.Bold, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(12.dp))

        ProfileRowItem(
          icon = Icons.Default.Edit,
          title = "Edit Personal Details",
          subtitle = "Update contact, allergies & address",
          onClick = { showEditDialog = true },
          testTag = "row_edit_profile"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        ProfileRowItem(
          icon = Icons.Default.Warning,
          title = "Known Allergies",
          subtitle = patientProfile.allergies.joinToString(", ").ifEmpty { "None recorded" },
          onClick = { showEditDialog = true }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        ProfileRowItem(
          icon = Icons.Default.Security,
          title = "Privacy & Consent",
          subtitle = "Manage health data usage and terms",
          onClick = {}
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Preferences & Settings Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("App Preferences", fontWeight = FontWeight.Bold, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Dark Theme", fontWeight = FontWeight.Medium, fontSize = 14.sp)
          }
          Switch(
            checked = isDarkTheme,
            onCheckedChange = { onToggleTheme() },
            modifier = Modifier.testTag("switch_dark_theme")
          )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = ClinicalTeal, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Appointment Reminders", fontWeight = FontWeight.Medium, fontSize = 14.sp)
          }
          Switch(
            checked = smsReminders,
            onCheckedChange = { smsReminders = it }
          )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Queue, contentDescription = null, tint = MedicalBluePrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Live Queue Token Alerts", fontWeight = FontWeight.Medium, fontSize = 14.sp)
          }
          Switch(
            checked = queueAlerts,
            onCheckedChange = { queueAlerts = it }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Doctor App Switcher Shortcut
    OutlinedButton(
      onClick = onSwitchToDoctorApp,
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("btn_switch_to_doctor_from_profile"),
      shape = RoundedCornerShape(12.dp)
    ) {
      Icon(Icons.Default.MedicalServices, contentDescription = null, tint = ClinicalTeal)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Switch to Get Well Soon Doctor App", fontWeight = FontWeight.SemiBold)
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Sign Out Button
    Button(
      onClick = onSignOut,
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("btn_patient_sign_out"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
    ) {
      Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text("Sign Out", fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(100.dp))
  }

  // Edit Profile Dialog
  if (showEditDialog) {
    var editName by remember { mutableStateOf(patientProfile.fullName) }
    var editPhone by remember { mutableStateOf(patientProfile.phone) }
    var editEmail by remember { mutableStateOf(patientProfile.email) }
    var editBlood by remember { mutableStateOf(patientProfile.bloodGroup) }
    var editEmergency by remember { mutableStateOf(patientProfile.emergencyContact) }

    AlertDialog(
      onDismissRequest = { showEditDialog = false },
      title = { Text("Edit Patient Profile", fontWeight = FontWeight.Bold) },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          OutlinedTextField(
            value = editName,
            onValueChange = { editName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = editPhone,
            onValueChange = { editPhone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = editEmail,
            onValueChange = { editEmail = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = editBlood,
            onValueChange = { editBlood = it },
            label = { Text("Blood Group (e.g. O+, A+)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = editEmergency,
            onValueChange = { editEmergency = it },
            label = { Text("Emergency Contact") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
          )
        }
      },
      confirmButton = {
        Button(onClick = {
          repository.updatePatientProfile(
            patientProfile.copy(
              fullName = editName,
              phone = editPhone,
              email = editEmail,
              bloodGroup = editBlood,
              emergencyContact = editEmergency
            )
          )
          showEditDialog = false
        }) {
          Text("Save Changes")
        }
      },
      dismissButton = {
        TextButton(onClick = { showEditDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun ProfileRowItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit,
  testTag: String = ""
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(vertical = 6.dp)
      .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
      Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Icon(
      imageVector = Icons.Default.ChevronRight,
      contentDescription = null,
      tint = Color.Gray,
      modifier = Modifier.size(18.dp)
    )
  }
}
