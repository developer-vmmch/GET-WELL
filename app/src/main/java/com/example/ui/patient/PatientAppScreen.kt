package com.example.ui.patient

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
fun PatientAppScreen(
  repository: HealthcareRepository,
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  onSwitchMode: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Home, 1: Discover, 2: Appointments, 3: Profile
  var doctorToBook by remember { mutableStateOf<Doctor?>(null) }
  var discoverSpecialtyFilter by remember { mutableStateOf<Specialty?>(null) }

  val patientProfile by repository.patientProfile.collectAsState()
  val doctors by repository.doctors.collectAsState()
  val clinics by repository.clinics.collectAsState()
  val appointments by repository.appointments.collectAsState()
  val session by repository.patientSession.collectAsState()

  // Handle hardware back press
  BackHandler(enabled = selectedTab != 0 || doctorToBook != null) {
    if (doctorToBook != null) {
      doctorToBook = null
    } else {
      selectedTab = 0
    }
  }

  if (!session.isAuthenticated) {
    PatientAuthScreen(
      onLoginSuccess = { phone, name ->
        repository.loginPatient(phone, name)
      },
      onSkipPreview = {
        repository.loginPatient("+1 555-234-8901", "Alex Morgan")
      }
    )
    return
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      AmtEcosystemHeader(
        currentMode = "PATIENT",
        onSwitchMode = onSwitchMode,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme
      )
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier
          .windowInsetsPadding(WindowInsets.navigationBars)
          .testTag("patient_bottom_nav"),
        tonalElevation = 6.dp
      ) {
        NavigationBarItem(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          icon = {
            Icon(
              if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
              contentDescription = "Home"
            )
          },
          label = { Text("Home") },
          modifier = Modifier.testTag("nav_item_home")
        )

        NavigationBarItem(
          selected = selectedTab == 1,
          onClick = {
            discoverSpecialtyFilter = null
            selectedTab = 1
          },
          icon = {
            Icon(
              if (selectedTab == 1) Icons.Filled.Search else Icons.Outlined.Search,
              contentDescription = "Discover"
            )
          },
          label = { Text("Discover") },
          modifier = Modifier.testTag("nav_item_discover")
        )

        val upcomingCount = remember(appointments) {
          appointments.count {
            it.patientId == patientProfile.id &&
              (it.status == AppointmentStatus.CONFIRMED || it.status == AppointmentStatus.CHECKED_IN)
          }
        }

        NavigationBarItem(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          icon = {
            BadgedBox(
              badge = {
                if (upcomingCount > 0) {
                  Badge { Text(upcomingCount.toString()) }
                }
              }
            ) {
              Icon(
                if (selectedTab == 2) Icons.Filled.CalendarMonth else Icons.Outlined.CalendarMonth,
                contentDescription = "Appointments"
              )
            }
          },
          label = { Text("Appointments") },
          modifier = Modifier.testTag("nav_item_appointments")
        )

        NavigationBarItem(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          icon = {
            Icon(
              if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person,
              contentDescription = "Profile"
            )
          },
          label = { Text("Profile") },
          modifier = Modifier.testTag("nav_item_profile")
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
          val nextUpcoming = remember(appointments, patientProfile.id) {
            appointments.firstOrNull {
              it.patientId == patientProfile.id &&
                (it.status == AppointmentStatus.CONFIRMED || it.status == AppointmentStatus.CHECKED_IN)
            }
          }

          PatientHomeScreen(
            patientProfile = patientProfile,
            upcomingAppointment = nextUpcoming,
            doctors = doctors,
            clinics = clinics,
            onSelectDoctorToBook = { doc -> doctorToBook = doc },
            onViewDoctorProfile = { doc -> doctorToBook = doc },
            onNavigateToDiscover = { spec ->
              discoverSpecialtyFilter = spec
              selectedTab = 1
            },
            onViewAppointmentPass = { selectedTab = 2 },
            onChangeCity = { city -> repository.updatePatientCity(city) }
          )
        }

        1 -> {
          PatientDiscoverScreen(
            doctors = doctors,
            clinics = clinics,
            initialSpecialty = discoverSpecialtyFilter,
            onSelectDoctorToBook = { doc -> doctorToBook = doc },
            onViewDoctorProfile = { doc -> doctorToBook = doc }
          )
        }

        2 -> {
          val patientAppointments = remember(appointments, patientProfile.id) {
            appointments.filter { it.patientId == patientProfile.id }
          }

          PatientAppointmentsScreen(
            appointments = patientAppointments,
            repository = repository,
            onViewQrPass = { /* handled inside screen */ }
          )
        }

        3 -> {
          PatientProfileScreen(
            patientProfile = patientProfile,
            repository = repository,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            onSwitchToDoctorApp = { onSwitchMode("DOCTOR") },
            onSignOut = { repository.logoutPatient() }
          )
        }
      }

      // Booking Sheet Modal
      doctorToBook?.let { doc ->
        PatientBookingSheet(
          doctor = doc,
          patientProfile = patientProfile,
          repository = repository,
          onDismiss = { doctorToBook = null },
          onBookingComplete = {
            doctorToBook = null
            selectedTab = 2 // navigate to appointments to view newly booked pass!
          }
        )
      }
    }
  }
}
