package com.example.ui.launcher

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.common.MedicalNetworkBackground
import com.example.ui.theme.*

@Composable
fun EcosystemLaunchScreen(
  onLaunchPatientApp: () -> Unit,
  onLaunchDoctorApp: () -> Unit,
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {
    MedicalNetworkBackground(isDoctor = false)

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
        .padding(horizontal = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      item {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Brand Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                  Brush.linearGradient(listOf(MedicalBluePrimary, ClinicalTeal))
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.HealthAndSafety,
                contentDescription = "CSVS IT Tech Logo",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "CSVS IT Tech",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 1.2.sp,
                  color = MedicalBluePrimary
                )
              )
              Text(
                text = "Get Well Soon",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          IconButton(
            onClick = onToggleTheme,
            modifier = Modifier.testTag("launcher_theme_toggle")
          ) {
            Icon(
              imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
              contentDescription = "Toggle Theme",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Hero Banner
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .testTag("ecosystem_hero_banner"),
          colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
          )
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.verticalGradient(
                  listOf(
                    DoctorNavyPrimary,
                    Color(0xFF0F2B48)
                  )
                )
              )
              .padding(24.dp)
          ) {
            Column {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(DoctorTealAccent.copy(alpha = 0.2f))
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "HEALTHCARE ECOSYSTEM PLATFORM",
                  color = DoctorAccentSky,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp
                )
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "Next-Generation\nClinic & Doctor Booking",
                style = MaterialTheme.typography.headlineMedium.copy(
                  fontWeight = FontWeight.ExtraBold,
                  lineHeight = 32.sp
                ),
                color = Color.White
              )

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "Seamless two-app healthcare architecture powered by CSVS IT Tech. Connected schedule management, token queues, and digital clinic check-in.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFCBD5E1)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title
        Text(
          text = "Select Application to Launch",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
        )
      }

      // App Card 1: Patient App
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
              1.5.dp,
              MedicalBluePrimary.copy(alpha = 0.3f),
              RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onLaunchPatientApp)
            .testTag("launch_patient_app_card"),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .background(MedicalBlueLight),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = MedicalBluePrimary,
                  modifier = Modifier.size(26.dp)
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(ClinicalTealLight)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "FOR PATIENTS",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = ClinicalTealDark
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "Get Well Soon — Patient App",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "“Your care, within reach.”",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = MedicalBluePrimary
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Discover top clinics and specialists, select live token slots, receive digital check-in passes, and track appointment history.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = onLaunchPatientApp,
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("btn_launch_patient"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MedicalBluePrimary)
            ) {
              Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Open Patient App", fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // App Card 2: Doctor App
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(
              1.5.dp,
              DoctorNavyPrimary.copy(alpha = 0.4f),
              RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onLaunchDoctorApp)
            .testTag("launch_doctor_app_card"),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .background(DoctorNavyPrimary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.MedicalServices,
                  contentDescription = null,
                  tint = DoctorAccentSky,
                  modifier = Modifier.size(26.dp)
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(DoctorNavyPrimary.copy(alpha = 0.1f))
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "FOR DOCTORS",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = DoctorNavyPrimary
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "Get Well Soon — Doctor App",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )

            Text(
              text = "“Your practice, organized.”",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = ClinicalTeal
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = "Review clinic-assigned daily schedules, manage live patient queues, conduct consultations, update status, and manage slot capacity.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
              onClick = onLaunchDoctorApp,
              modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("btn_launch_doctor"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = DoctorNavyPrimary)
            ) {
              Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Open Doctor App", fontWeight = FontWeight.Bold)
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }

      // Feature highlights
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Platform Highlights",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ClinicalTeal, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Atomic double-booking prevention engine", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ClinicalTeal, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Real-time synchronization between Doctor and Patient", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ClinicalTeal, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Digital QR appointment passes for clinic check-in", fontSize = 12.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}
