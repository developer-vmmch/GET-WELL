package com.example.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun AmtEcosystemHeader(
  currentMode: String,
  onSwitchMode: (String) -> Unit,
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    tonalElevation = 2.dp,
    shadowElevation = 1.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(
                Brush.linearGradient(
                  listOf(
                    if (currentMode == "DOCTOR") DoctorNavyPrimary else MedicalBluePrimary,
                    if (currentMode == "DOCTOR") DoctorTealAccent else ClinicalTeal
                  )
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (currentMode == "DOCTOR") Icons.Default.MedicalServices else Icons.Default.HealthAndSafety,
              contentDescription = "AMT Logo",
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "AMT TECH NOVA",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 1.sp,
                  fontSize = 10.sp
                ),
                color = if (currentMode == "DOCTOR") DoctorTealAccent else MedicalBluePrimary
              )
              Spacer(modifier = Modifier.width(4.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(MaterialTheme.colorScheme.primaryContainer)
                  .padding(horizontal = 4.dp, vertical = 1.dp)
              ) {
                Text(
                  text = "ECOSYSTEM",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.onPrimaryContainer
                )
              }
            }
            Text(
              text = when (currentMode) {
                "DOCTOR" -> "Get Well Soon • Doctor"
                "PATIENT" -> "Get Well Soon • Patient"
                else -> "Get Well Soon Healthcare"
              },
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = onToggleTheme,
            modifier = Modifier
              .size(38.dp)
              .testTag("theme_toggle_button")
          ) {
            Icon(
              imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
              contentDescription = "Toggle Theme",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(20.dp)
            )
          }

          Spacer(modifier = Modifier.width(4.dp))

          // Switcher Menu Button
          Box {
            var expanded by remember { mutableStateOf(false) }
            Button(
              onClick = { expanded = true },
              shape = RoundedCornerShape(20.dp),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
              ),
              modifier = Modifier
                .height(34.dp)
                .testTag("app_switcher_button")
            ) {
              Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "Switch App",
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = when (currentMode) {
                  "DOCTOR" -> "Doctor App"
                  "PATIENT" -> "Patient App"
                  else -> "Hub"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            DropdownMenu(
              expanded = expanded,
              onDismissRequest = { expanded = false },
              modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Dashboard,
                      contentDescription = null,
                      tint = MedicalBluePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Text("Ecosystem Launchpad", fontWeight = FontWeight.Bold)
                      Text("Reference landing & switcher", fontSize = 11.sp, color = Color.Gray)
                    }
                  }
                },
                onClick = {
                  expanded = false
                  onSwitchMode("LAUNCHER")
                },
                modifier = Modifier.testTag("switch_to_launcher")
              )

              HorizontalDivider()

              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Person,
                      contentDescription = null,
                      tint = MedicalBluePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Text("Get Well Soon Patient", fontWeight = FontWeight.Bold)
                      Text("Find doctors, book slots", fontSize = 11.sp, color = Color.Gray)
                    }
                  }
                },
                onClick = {
                  expanded = false
                  onSwitchMode("PATIENT")
                },
                modifier = Modifier.testTag("switch_to_patient")
              )

              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.MedicalServices,
                      contentDescription = null,
                      tint = ClinicalTeal
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                      Text("Get Well Soon Doctor", fontWeight = FontWeight.Bold)
                      Text("Schedules, consults, tokens", fontSize = 11.sp, color = Color.Gray)
                    }
                  }
                },
                onClick = {
                  expanded = false
                  onSwitchMode("DOCTOR")
                },
                modifier = Modifier.testTag("switch_to_doctor")
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun AppointmentStatusBadge(
  status: AppointmentStatus,
  modifier: Modifier = Modifier
) {
  val (color, bgColor) = when (status) {
    AppointmentStatus.CONFIRMED -> StatusConfirmed to MedicalBlueLight
    AppointmentStatus.CHECKED_IN -> StatusCheckedIn to Color(0xFFEDE9FE)
    AppointmentStatus.IN_CONSULTATION -> StatusInConsultation to Color(0xFFFEF3C7)
    AppointmentStatus.COMPLETED -> StatusCompleted to Color(0xFFD1FAE5)
    AppointmentStatus.CANCELLED -> StatusCancelled to Color(0xFFFEE2E2)
    AppointmentStatus.PENDING -> Color(0xFF6B7280) to Color(0xFFF3F4F6)
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val alpha by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  Row(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (status == AppointmentStatus.CHECKED_IN || status == AppointmentStatus.IN_CONSULTATION) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(color.copy(alpha = alpha))
      )
      Spacer(modifier = Modifier.width(5.dp))
    }
    Text(
      text = status.label,
      color = color,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
fun DoctorCard(
  doctor: Doctor,
  onBookClick: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .clickable(onClick = onProfileClick)
      .testTag("doctor_card_${doctor.id}"),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(verticalAlignment = Alignment.Top) {
        // Doctor Avatar with Specialty Initial
        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
              Brush.linearGradient(
                listOf(
                  Color(doctor.avatarColorHex),
                  Color(doctor.avatarColorHex).copy(alpha = 0.7f)
                )
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = doctor.name.split(" ").let { parts ->
              if (parts.size >= 2) "${parts[0].first()}${parts[1].first()}" else "DR"
            },
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = doctor.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              if (doctor.verified) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.Default.Verified,
                  contentDescription = "Verified Doctor",
                  tint = ClinicalTeal,
                  modifier = Modifier.size(16.dp)
                )
              }
            }

            // Rating Pill
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFEF3C7))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFD97706),
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = doctor.rating.toString(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E)
              )
            }
          }

          Text(
            text = doctor.specialty.displayName,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.primary
          )

          Text(
            text = doctor.clinicName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Consultation Fee",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "$${doctor.consultationFee.toInt()}",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            tint = ClinicalTeal,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = doctor.nextAvailableSlot,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = ClinicalTeal
          )
        }

        Button(
          onClick = onBookClick,
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
          ),
          modifier = Modifier
            .height(36.dp)
            .testTag("book_button_${doctor.id}")
        ) {
          Text("Book", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun ClinicCard(
  clinic: Clinic,
  onClinicClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .clickable(onClick = onClinicClick)
      .testTag("clinic_card_${clinic.id}"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = clinic.name,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            if (clinic.verified) {
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = "Verified Clinic",
                tint = MedicalBluePrimary,
                modifier = Modifier.size(16.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(2.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${clinic.address} • ${clinic.distanceKm} km",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFEF3C7))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFD97706),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = clinic.rating.toString(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF92400E)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.AccessTime,
          contentDescription = null,
          tint = ClinicalTeal,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Open: ${clinic.openingHours}",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Services chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        clinic.services.take(3).forEach { service ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = service,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@Composable
fun QrPassCard(
  appointment: Appointment,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .testTag("digital_qr_pass_card"),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.QrCode2,
            contentDescription = null,
            tint = MedicalBluePrimary
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "DIGITAL APPOINTMENT PASS",
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MedicalBluePrimary,
            letterSpacing = 1.sp
          )
        }
        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Large Token Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(16.dp))
          .background(MedicalBlueLight)
          .padding(horizontal = 24.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "TOKEN NUMBER",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MedicalBlueDark
          )
          Text(
            text = "#${appointment.tokenNumber}",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MedicalBlueDark
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Realistic Canvas-drawn QR Code Graphic
      Box(
        modifier = Modifier
          .size(160.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color.White)
          .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
          .padding(12.dp),
        contentAlignment = Alignment.Center
      ) {
        Canvas(modifier = Modifier.size(136.dp)) {
          val squareSize = size.width / 15f
          val pattern = listOf(
            listOf(1,1,1,1,1,1,1,0,1,1,1,1,1,1,1),
            listOf(1,0,0,0,0,0,1,0,1,0,0,0,0,0,1),
            listOf(1,0,1,1,1,0,1,0,1,0,1,1,1,0,1),
            listOf(1,0,1,1,1,0,1,0,1,0,1,1,1,0,1),
            listOf(1,0,0,0,0,0,1,0,1,0,0,0,0,0,1),
            listOf(1,1,1,1,1,1,1,0,1,1,1,1,1,1,1),
            listOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
            listOf(1,0,1,1,0,1,0,1,0,1,1,0,1,0,1),
            listOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
            listOf(1,1,1,1,1,1,1,0,1,0,1,1,0,1,0),
            listOf(1,0,0,0,0,0,1,0,0,1,0,0,1,0,1),
            listOf(1,0,1,1,1,0,1,0,1,1,1,0,0,1,0),
            listOf(1,0,1,1,1,0,1,0,0,0,1,1,1,0,1),
            listOf(1,0,0,0,0,0,1,0,1,0,0,1,0,1,0),
            listOf(1,1,1,1,1,1,1,0,1,1,0,1,1,0,1)
          )

          pattern.forEachIndexed { row, cols ->
            cols.forEachIndexed { col, bit ->
              if (bit == 1) {
                drawRect(
                  color = Color(0xFF0F172A),
                  topLeft = Offset(col * squareSize, row * squareSize),
                  size = Size(squareSize - 1f, squareSize - 1f)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      Text(
        text = "Scan at Reception Kiosk for Instant Check-in",
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))
      HorizontalDivider()
      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("Doctor", fontSize = 11.sp, color = Color.Gray)
          Text(appointment.doctorName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Text(appointment.doctorSpecialty, fontSize = 11.sp, color = MedicalBluePrimary)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("Date & Time", fontSize = 11.sp, color = Color.Gray)
          Text("${appointment.date} • ${appointment.time}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          AppointmentStatusBadge(status = appointment.status)
        }
      }
    }
  }
}

@Composable
fun MedicalNetworkBackground(
  isDoctor: Boolean = false,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_network")
  val phase by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 6.28f,
    animationSpec = infiniteRepeatable(
      animation = tween(12000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "network_phase"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .drawBehind {
        val baseColor = if (isDoctor) DoctorNavyPrimary else Color(0xFFF0F7FD)
        drawRect(baseColor)

        // Draw soft medical grid nodes
        val nodeColor = if (isDoctor) DoctorAccentSky.copy(alpha = 0.08f) else MedicalBluePrimary.copy(alpha = 0.06f)
        val step = 60.dp.toPx()

        var x = 20.dp.toPx()
        while (x < size.width) {
          var y = 20.dp.toPx()
          while (y < size.height) {
            val offsetRadius = 3f + kotlin.math.sin(phase + (x * 0.01f) + (y * 0.01f)).toFloat() * 1.5f
            drawCircle(
              color = nodeColor,
              radius = offsetRadius,
              center = Offset(x, y)
            )
            y += step
          }
          x += step
        }
      }
  )
}
