package com.example.ui.patient

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.common.*
import com.example.ui.theme.*

@Composable
fun PatientHomeScreen(
  patientProfile: PatientProfile,
  upcomingAppointment: Appointment?,
  doctors: List<Doctor>,
  clinics: List<Clinic>,
  onSelectDoctorToBook: (Doctor) -> Unit,
  onViewDoctorProfile: (Doctor) -> Unit,
  onNavigateToDiscover: (specialtyFilter: Specialty?) -> Unit,
  onViewAppointmentPass: (Appointment) -> Unit,
  onChangeCity: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var showCityDialog by remember { mutableStateOf(false) }

  val supportedCities = listOf(
    "San Francisco, CA",
    "New York, NY",
    "Los Angeles, CA",
    "Chicago, IL",
    "London, UK",
    "Singapore"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // 1. Patient Welcome & Location Selector
    item {
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Welcome back,",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = patientProfile.fullName,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        // Location Pill
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { showCityDialog = true }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag("location_selector_pill"),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = patientProfile.selectedCity.split(",").firstOrNull() ?: "Location",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // 2. Search Shortcut Bar
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .clickable { onNavigateToDiscover(null) }
          .testTag("home_search_bar_shortcut"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = "Search doctors, clinics, or specialties...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }

    // 3. Upcoming Appointment Hero Card (if any exists)
    upcomingAppointment?.let { apt ->
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .testTag("home_upcoming_appointment_card"),
          colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  listOf(MedicalBluePrimary, MedicalBlueDark)
                )
              )
              .padding(18.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "UPCOMING VISIT",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                  )
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = "Token #${apt.tokenNumber}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = apt.doctorName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )

              Text(
                text = "${apt.doctorSpecialty} • ${apt.clinicName}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${apt.date} at ${apt.time}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }

                Button(
                  onClick = { onViewAppointmentPass(apt) },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MedicalBlueDark
                  ),
                  contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                  modifier = Modifier
                    .height(32.dp)
                    .testTag("btn_view_pass_hero")
                ) {
                  Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Digital Pass", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }

    // 4. Specialty Shortcuts
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Medical Specialties",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        TextButton(onClick = { onNavigateToDiscover(null) }) {
          Text("View All", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
      ) {
        items(Specialty.entries.toTypedArray()) { specialty ->
          Card(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .clickable { onNavigateToDiscover(specialty) }
              .testTag("specialty_chip_${specialty.name}"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
          ) {
            Column(
              modifier = Modifier
                .width(100.dp)
                .padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(MedicalBlueLight),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (specialty) {
                    Specialty.CARDIOLOGY -> Icons.Default.Favorite
                    Specialty.DERMATOLOGY -> Icons.Default.Face
                    Specialty.PEDIATRICS -> Icons.Default.ChildCare
                    Specialty.ORTHOPEDICS -> Icons.Default.Accessibility
                    Specialty.NEUROLOGY -> Icons.Default.Psychology
                    Specialty.GENERAL_MEDICINE -> Icons.Default.LocalHospital
                    Specialty.DENTISTRY -> Icons.Default.MedicalServices
                    Specialty.OPHTHALMOLOGY -> Icons.Default.Visibility
                  },
                  contentDescription = specialty.displayName,
                  tint = MedicalBluePrimary,
                  modifier = Modifier.size(22.dp)
                )
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = specialty.displayName,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }

    // 5. Featured Doctors
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Top Verified Doctors",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        TextButton(onClick = { onNavigateToDiscover(null) }) {
          Text("See All", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
    }

    items(doctors.take(3)) { doc ->
      DoctorCard(
        doctor = doc,
        onBookClick = { onSelectDoctorToBook(doc) },
        onProfileClick = { onViewDoctorProfile(doc) },
        modifier = Modifier.padding(bottom = 12.dp)
      )
    }

    // 6. Nearby Clinics
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Nearby Partner Clinics",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
    }

    items(clinics.take(2)) { clinic ->
      ClinicCard(
        clinic = clinic,
        onClinicClick = { onNavigateToDiscover(null) },
        modifier = Modifier.padding(bottom = 12.dp)
      )
    }
  }

  // City Picker Dialog
  if (showCityDialog) {
    AlertDialog(
      onDismissRequest = { showCityDialog = false },
      title = { Text("Select Your Location", fontWeight = FontWeight.Bold) },
      text = {
        Column {
          supportedCities.forEach { city ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                  onChangeCity(city)
                  showCityDialog = false
                }
                .padding(vertical = 12.dp, horizontal = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.LocationCity,
                contentDescription = null,
                tint = if (patientProfile.selectedCity == city) MedicalBluePrimary else Color.Gray,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = city,
                fontWeight = if (patientProfile.selectedCity == city) FontWeight.Bold else FontWeight.Normal,
                color = if (patientProfile.selectedCity == city) MedicalBluePrimary else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showCityDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
