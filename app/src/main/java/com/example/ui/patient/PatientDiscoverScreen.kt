package com.example.ui.patient

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.model.*
import com.example.ui.common.*
import com.example.ui.theme.*

@Composable
fun PatientDiscoverScreen(
  doctors: List<Doctor>,
  clinics: List<Clinic>,
  initialSpecialty: Specialty?,
  onSelectDoctorToBook: (Doctor) -> Unit,
  onViewDoctorProfile: (Doctor) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedSpecialty by remember { mutableStateOf(initialSpecialty) }
  var selectedTab by remember { mutableIntStateOf(0) } // 0: Doctors, 1: Clinics
  var consultationFilter by remember { mutableStateOf<ConsultationMode?>(null) }

  // Filter doctors
  val filteredDoctors = remember(searchQuery, selectedSpecialty, consultationFilter, doctors) {
    doctors.filter { doc ->
      val matchesSearch = searchQuery.isBlank() ||
        doc.name.contains(searchQuery, ignoreCase = true) ||
        doc.specialty.displayName.contains(searchQuery, ignoreCase = true) ||
        doc.clinicName.contains(searchQuery, ignoreCase = true)

      val matchesSpecialty = selectedSpecialty == null || doc.specialty == selectedSpecialty

      val matchesMode = consultationFilter == null ||
        doc.consultationMode == ConsultationMode.BOTH ||
        doc.consultationMode == consultationFilter

      matchesSearch && matchesSpecialty && matchesMode
    }
  }

  // Filter clinics
  val filteredClinics = remember(searchQuery, clinics) {
    clinics.filter { clinic ->
      searchQuery.isBlank() ||
        clinic.name.contains(searchQuery, ignoreCase = true) ||
        clinic.address.contains(searchQuery, ignoreCase = true) ||
        clinic.services.any { it.contains(searchQuery, ignoreCase = true) }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Search Box
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Search by doctor, clinic, specialty...") },
      leadingIcon = {
        Icon(
          Icons.Default.Search,
          contentDescription = "Search",
          tint = MaterialTheme.colorScheme.primary
        )
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear")
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("discover_search_input"),
      singleLine = true,
      shape = RoundedCornerShape(14.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Tabs: Doctors / Clinics
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
      modifier = Modifier.clip(RoundedCornerShape(12.dp))
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Text(
            "Doctors (${filteredDoctors.size})",
            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
          )
        },
        modifier = Modifier.testTag("tab_discover_doctors")
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Text(
            "Clinics (${filteredClinics.size})",
            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
          )
        },
        modifier = Modifier.testTag("tab_discover_clinics")
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Specialty horizontal filter chips (only for doctors tab)
    if (selectedTab == 0) {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
      ) {
        item {
          FilterChip(
            selected = selectedSpecialty == null,
            onClick = { selectedSpecialty = null },
            label = { Text("All Specialties") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("chip_all_specialties")
          )
        }
        items(Specialty.entries.toTypedArray()) { spec ->
          FilterChip(
            selected = selectedSpecialty == spec,
            onClick = {
              selectedSpecialty = if (selectedSpecialty == spec) null else spec
            },
            label = { Text(spec.displayName) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("chip_${spec.name}")
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Results List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      if (selectedTab == 0) {
        if (filteredDoctors.isEmpty()) {
          item {
            EmptySearchState(query = searchQuery, onReset = {
              searchQuery = ""
              selectedSpecialty = null
            })
          }
        } else {
          items(filteredDoctors) { doc ->
            DoctorCard(
              doctor = doc,
              onBookClick = { onSelectDoctorToBook(doc) },
              onProfileClick = { onViewDoctorProfile(doc) },
              modifier = Modifier.padding(bottom = 12.dp)
            )
          }
        }
      } else {
        if (filteredClinics.isEmpty()) {
          item {
            EmptySearchState(query = searchQuery, onReset = { searchQuery = "" })
          }
        } else {
          items(filteredClinics) { clinic ->
            ClinicCard(
              clinic = clinic,
              onClinicClick = {
                // filter doctors to this clinic and switch to doctors tab
                searchQuery = clinic.name
                selectedTab = 0
              },
              modifier = Modifier.padding(bottom = 12.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun EmptySearchState(
  query: String,
  onReset: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(40.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = Icons.Default.SearchOff,
      contentDescription = null,
      tint = Color.Gray,
      modifier = Modifier.size(56.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
      text = if (query.isNotEmpty()) "No results matching “$query”" else "No matching doctors found",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Try adjusting your search terms or specialty filters.",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = onReset,
      shape = RoundedCornerShape(10.dp)
    ) {
      Text("Reset Filters")
    }
  }
}
