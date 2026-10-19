package com.example.ui.doctor

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.common.MedicalNetworkBackground
import com.example.ui.theme.*

@Composable
fun DoctorAuthScreen(
  onLoginSuccess: (licenseNumber: String) -> Unit,
  onQuickLoginDrJenkins: () -> Unit,
  modifier: Modifier = Modifier
) {
  var licenseNumber by remember { mutableStateOf("MED-US-89421") }
  var password by remember { mutableStateOf("••••••••") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Box(modifier = modifier.fillMaxSize()) {
    MedicalNetworkBackground(isDoctor = true)

    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(20.dp))

      // Doctor Badge
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(listOf(DoctorNavyPrimary, DoctorNavySurface))
          )
          .border(2.dp, DoctorAccentSky.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.MedicalServices,
          contentDescription = "Doctor Icon",
          tint = DoctorAccentSky,
          modifier = Modifier.size(40.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Get Well Soon Doctor",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
        color = Color.White
      )

      Text(
        text = "“Your practice, organized.”",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          color = DoctorAccentSky
        )
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Authorized medical practitioner portal for clinic schedules, patient queues, and consultations.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = Color(0xFF94A3B8)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Form Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = DoctorNavySecondary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = "Clinic Authorization Login",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = licenseNumber,
            onValueChange = {
              licenseNumber = it
              errorMessage = null
            },
            label = { Text("Medical License / Practitioner ID", color = Color(0xFF94A3B8)) },
            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = DoctorAccentSky) },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = DoctorAccentSky,
              unfocusedBorderColor = Color(0xFF475569)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_doctor_license"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = password,
            onValueChange = {
              password = it
              errorMessage = null
            },
            label = { Text("Secure Password", color = Color(0xFF94A3B8)) },
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = DoctorAccentSky) },
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedBorderColor = DoctorAccentSky,
              unfocusedBorderColor = Color(0xFF475569)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_doctor_password"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
          )

          errorMessage?.let { err ->
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = err,
              color = Color(0xFFF87171),
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Button(
            onClick = {
              if (licenseNumber.isBlank()) {
                errorMessage = "Please enter your practitioner license number."
              } else {
                onLoginSuccess(licenseNumber)
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("btn_doctor_sign_in"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DoctorTealAccent)
          ) {
            Text("Access Doctor Console", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DoctorNavyPrimary)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Preset Authorized Doctor Profiles for Instant Testing
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E36))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Verified Clinic Roster (1-Tap Test Login):",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = DoctorAccentSky
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFF1E293B))
              .clickable(onClick = onQuickLoginDrJenkins)
              .padding(10.dp)
              .testTag("quick_login_dr_jenkins"),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MedicalBluePrimary),
              contentAlignment = Alignment.Center
            ) {
              Text("SJ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Dr. Sarah Jenkins, MD", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              Text("Cardiology • Nova Heart Institute", color = Color(0xFF94A3B8), fontSize = 11.sp)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = DoctorAccentSky, modifier = Modifier.size(16.dp))
          }
        }
      }
    }
  }
}
