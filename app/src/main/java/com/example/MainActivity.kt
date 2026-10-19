package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.HealthcareRepository
import com.example.ui.doctor.DoctorAppScreen
import com.example.ui.launcher.EcosystemLaunchScreen
import com.example.ui.patient.PatientAppScreen
import com.example.ui.theme.GetWellSoonTheme

class MainActivity : ComponentActivity() {

  private val repository = HealthcareRepository.instance

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val systemInDark = isSystemInDarkTheme()
      var isDarkTheme by remember { mutableStateOf(systemInDark) }
      // Modes: "LAUNCHER", "PATIENT", "DOCTOR"
      var currentMode by remember { mutableStateOf("LAUNCHER") }

      BackHandler(enabled = currentMode != "LAUNCHER") {
        currentMode = "LAUNCHER"
      }

      GetWellSoonTheme(
        isDoctorApp = currentMode == "DOCTOR",
        darkTheme = isDarkTheme
      ) {
        Crossfade(
          targetState = currentMode,
          label = "app_mode_crossfade",
          modifier = Modifier.fillMaxSize()
        ) { mode ->
          when (mode) {
            "LAUNCHER" -> {
              EcosystemLaunchScreen(
                onLaunchPatientApp = { currentMode = "PATIENT" },
                onLaunchDoctorApp = { currentMode = "DOCTOR" },
                isDarkTheme = isDarkTheme,
                onToggleTheme = { isDarkTheme = !isDarkTheme }
              )
            }

            "PATIENT" -> {
              PatientAppScreen(
                repository = repository,
                isDarkTheme = isDarkTheme,
                onToggleTheme = { isDarkTheme = !isDarkTheme },
                onSwitchMode = { newMode -> currentMode = newMode }
              )
            }

            "DOCTOR" -> {
              DoctorAppScreen(
                repository = repository,
                isDarkTheme = isDarkTheme,
                onToggleTheme = { isDarkTheme = !isDarkTheme },
                onSwitchMode = { newMode -> currentMode = newMode }
              )
            }
          }
        }
      }
    }
  }
}
