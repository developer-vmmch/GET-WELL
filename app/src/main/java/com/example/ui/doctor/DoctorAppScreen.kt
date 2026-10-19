package com.example.ui.doctor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.HealthcareRepository
import com.example.model.*
import com.example.ui.common.AmtEcosystemHeader

@Composable
fun DoctorAppScreen(
  repository: HealthcareRepository,
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  onSwitchMode: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Schedule, 2: Profile
  var activeConsultationAppointment by remember { mutableStateOf<Appointment?>(null) }

  val doctorProfile by repository.doctorProfile.collectAsState()
  val appointments by repository.appointments.collectAsState()
  val session by repository.doctorSession.collectAsState()

  // BackHandler
  BackHandler(enabled = selectedTab != 0 || activeConsultationAppointment != null) {
    if (activeConsultationAppointment != null) {
      activeConsultationAppointment = null
    } else {
      selectedTab = 0
    }
  }

  if (!session.isAuthenticated) {
    DoctorAuthScreen(
      onLoginSuccess = { license ->
        repository.loginDoctor(license)
      },
      onQuickLoginDrJenkins = {
        repository.loginDoctor("MED-US-89421")
      }
    )
    return
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      AmtEcosystemHeader(
        currentMode = "DOCTOR",
        onSwitchMode = onSwitchMode,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("doctor_bottom_nav"),
        tonalElevation = 6.dp
      ) {
        NavigationBarItem(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          icon = {
            Icon(
              if (selectedTab == 0) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
              contentDescription = "Dashboard"
            )
          },
          label = { Text("Dashboard") },
          modifier = Modifier.testTag("doc_nav_dashboard")
        )

        NavigationBarItem(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          icon = {
            Icon(
              if (selectedTab == 1) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
              contentDescription = "Schedule"
            )
          },
          label = { Text("Schedule") },
          modifier = Modifier.testTag("doc_nav_schedule")
        )

        NavigationBarItem(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          icon = {
            Icon(
              if (selectedTab == 2) Icons.Filled.MedicalServices else Icons.Outlined.MedicalServices,
              contentDescription = "Profile"
            )
          },
          label = { Text("Profile") },
          modifier = Modifier.testTag("doc_nav_profile")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        0 -> {
          DoctorDashboardScreen(
            doctorProfile = doctorProfile,
            appointments = appointments,
            repository = repository,
            onSelectAppointment = { apt -> activeConsultationAppointment = apt },
            onNavigateToSchedule = { selectedTab = 1 }
          )
        }

        1 -> {
          DoctorScheduleScreen(
            doctorProfile = doctorProfile,
            appointments = appointments,
            repository = repository,
            onSelectAppointment = { apt -> activeConsultationAppointment = apt }
          )
        }

        2 -> {
          DoctorProfileScreen(
            doctorProfile = doctorProfile,
            repository = repository,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            onSwitchToPatientApp = { onSwitchMode("PATIENT") },
            onSignOut = { repository.logoutDoctor() }
          )
        }
      }

      // Consultation Sheet Modal
      activeConsultationAppointment?.let { apt ->
        // Refresh appointment state from latest list
        val currentApt = appointments.firstOrNull { it.id == apt.id } ?: apt
        DoctorConsultationSheet(
          appointment = currentApt,
          repository = repository,
          onDismiss = { activeConsultationAppointment = null }
        )
      }
    }
  }
}
