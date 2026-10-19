package com.example.ui.doctor

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
import com.example.model.DoctorProfile
import com.example.ui.theme.*

@Composable
fun DoctorProfileScreen(
  doctorProfile: DoctorProfile,
  repository: HealthcareRepository,
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  onSwitchToPatientApp: () -> Unit,
  onSignOut: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // Doctor Avatar
    Box(
      modifier = Modifier
        .size(80.dp)
        .clip(CircleShape)
        .background(DoctorNavyPrimary),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = "SJ",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = doctorProfile.fullName,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.width(6.dp))
      Icon(Icons.Default.Verified, contentDescription = "Verified", tint = ClinicalTeal, modifier = Modifier.size(18.dp))
    }

    Text(
      text = "${doctorProfile.title} • License: ${doctorProfile.medicalLicenseNumber}",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Doctor Specs Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(16.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceAround
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Specialty", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(doctorProfile.specialty.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MedicalBluePrimary)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Experience", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("${doctorProfile.experienceYears} Years", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Room", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(doctorProfile.roomNumber, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Clinic Affiliation Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Clinic Authorization & Credentials", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Domain, contentDescription = null, tint = MedicalBluePrimary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("Affiliated Institute", fontSize = 11.sp, color = Color.Gray)
            Text(doctorProfile.clinicAffiliation, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.AccessTime, contentDescription = null, tint = ClinicalTeal, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("Scheduled Shift", fontSize = 11.sp, color = Color.Gray)
            Text(doctorProfile.todayWorkingHours, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("Professional Contact", fontSize = 11.sp, color = Color.Gray)
            Text(doctorProfile.email, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Theme & Preferences
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
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
            modifier = Modifier.testTag("doc_switch_dark_theme")
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Switch to Patient App Button
    OutlinedButton(
      onClick = onSwitchToPatientApp,
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("btn_switch_to_patient_from_doc"),
      shape = RoundedCornerShape(12.dp)
    ) {
      Icon(Icons.Default.Person, contentDescription = null, tint = MedicalBluePrimary)
      Spacer(modifier = Modifier.width(8.dp))
      Text("Switch to Get Well Soon Patient App", fontWeight = FontWeight.SemiBold)
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Sign Out Button
    Button(
      onClick = onSignOut,
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("btn_doctor_sign_out"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
    ) {
      Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text("Sign Out", fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(100.dp))
  }
}
