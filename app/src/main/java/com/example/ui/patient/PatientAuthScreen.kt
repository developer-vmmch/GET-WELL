package com.example.ui.patient

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.common.MedicalNetworkBackground
import com.example.ui.theme.*

@Composable
fun PatientAuthScreen(
  onLoginSuccess: (phone: String, name: String) -> Unit,
  onSkipPreview: () -> Unit,
  modifier: Modifier = Modifier
) {
  var isRegisterMode by remember { mutableStateOf(false) }
  var phone by remember { mutableStateOf("+1 555-234-8901") }
  var fullName by remember { mutableStateOf("Alex Morgan") }
  var otpInput by remember { mutableStateOf("") }
  var otpSent by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var consentAgreed by remember { mutableStateOf(true) }

  Box(modifier = modifier.fillMaxSize()) {
    MedicalNetworkBackground(isDoctor = false)

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

      // Calming Logo & Illustration
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(listOf(MedicalBluePrimary, ClinicalTeal))
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.HealthAndSafety,
          contentDescription = "Healthcare Icon",
          tint = Color.White,
          modifier = Modifier.size(44.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Get Well Soon",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = "“Your care, within reach.”",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          color = MedicalBluePrimary
        )
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Find trusted clinics, book appointment slots, and manage your health seamlessly.",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Tab Switcher: Login / Register
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
        ) {
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (!isRegisterMode) MaterialTheme.colorScheme.surface else Color.Transparent)
              .clickable {
                isRegisterMode = false
                otpSent = false
                errorMessage = null
              }
              .padding(vertical = 10.dp)
              .testTag("tab_patient_login"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Sign In",
              fontWeight = if (!isRegisterMode) FontWeight.Bold else FontWeight.Medium,
              color = if (!isRegisterMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isRegisterMode) MaterialTheme.colorScheme.surface else Color.Transparent)
              .clickable {
                isRegisterMode = true
                otpSent = false
                errorMessage = null
              }
              .padding(vertical = 10.dp)
              .testTag("tab_patient_register"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Register",
              fontWeight = if (isRegisterMode) FontWeight.Bold else FontWeight.Medium,
              color = if (isRegisterMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Form Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          if (isRegisterMode) {
            OutlinedTextField(
              value = fullName,
              onValueChange = { fullName = it },
              label = { Text("Full Name") },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_patient_name"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
          }

          OutlinedTextField(
            value = phone,
            onValueChange = {
              phone = it
              errorMessage = null
            },
            label = { Text("Mobile Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("input_patient_phone"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
          )

          if (otpSent) {
            Spacer(modifier = Modifier.height(14.dp))

            // Dev OTP Helper banner
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(ClinicalTealLight)
                .padding(10.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = null,
                  tint = ClinicalTealDark,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Development OTP sent: 123456",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = ClinicalTealDark
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
              value = otpInput,
              onValueChange = {
                otpInput = it.take(6)
                errorMessage = null
              },
              label = { Text("Enter 6-digit OTP") },
              leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("input_patient_otp"),
              singleLine = true,
              shape = RoundedCornerShape(12.dp)
            )
          }

          if (isRegisterMode) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.clickable { consentAgreed = !consentAgreed }
            ) {
              Checkbox(
                checked = consentAgreed,
                onCheckedChange = { consentAgreed = it },
                modifier = Modifier.testTag("patient_consent_checkbox")
              )
              Text(
                text = "I agree to the Healthcare Privacy Notice and Terms of Service.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = error,
              color = MaterialTheme.colorScheme.error,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          if (!otpSent) {
            Button(
              onClick = {
                if (phone.length < 7) {
                  errorMessage = "Please enter a valid phone number."
                } else if (isRegisterMode && fullName.isBlank()) {
                  errorMessage = "Please enter your full name."
                } else if (isRegisterMode && !consentAgreed) {
                  errorMessage = "Please accept the privacy terms."
                } else {
                  otpSent = true
                  otpInput = "123456" // Pre-fill development code for fast testing
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_send_otp"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MedicalBluePrimary)
            ) {
              Text(
                text = if (isRegisterMode) "Proceed to Verify" else "Get Login OTP",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          } else {
            Button(
              onClick = {
                if (otpInput == "123456" || otpInput.length == 6) {
                  onLoginSuccess(phone, if (isRegisterMode) fullName else "Alex Morgan")
                } else {
                  errorMessage = "Invalid OTP. Use test code 123456."
                }
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_verify_otp"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = ClinicalTeal)
            ) {
              Text(
                text = "Verify & Access Care",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Direct Quick Access Button
      TextButton(
        onClick = onSkipPreview,
        modifier = Modifier.testTag("btn_patient_quick_preview")
      ) {
        Icon(
          imageVector = Icons.Default.FastForward,
          contentDescription = null,
          modifier = Modifier.size(16.dp),
          tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Continue as Alex Morgan (Quick Preview)",
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}
