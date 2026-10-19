package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val PatientLightColorScheme = lightColorScheme(
  primary = MedicalBluePrimary,
  onPrimary = Color.White,
  primaryContainer = MedicalBlueLight,
  onPrimaryContainer = MedicalBlueDark,
  secondary = ClinicalTeal,
  onSecondary = Color.White,
  secondaryContainer = ClinicalTealLight,
  onSecondaryContainer = ClinicalTealDark,
  tertiary = DoctorAccentSky,
  background = NeutralBackgroundLight,
  surface = NeutralSurfaceLight,
  onBackground = NeutralTextPrimaryLight,
  onSurface = NeutralTextPrimaryLight,
  surfaceVariant = NeutralSurfaceVariantLight,
  onSurfaceVariant = NeutralTextSecondaryLight,
  outline = NeutralBorderLight,
)

val PatientDarkColorScheme = darkColorScheme(
  primary = DoctorAccentSky,
  onPrimary = DoctorNavyPrimary,
  primaryContainer = DoctorNavySecondary,
  onPrimaryContainer = DoctorAccentSky,
  secondary = ClinicalTeal,
  onSecondary = Color.White,
  secondaryContainer = DoctorNavySurface,
  onSecondaryContainer = DoctorTealAccent,
  background = DarkBackground,
  surface = DarkSurface,
  onBackground = DarkTextPrimary,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder,
)

val DoctorLightColorScheme = lightColorScheme(
  primary = DoctorNavyPrimary,
  onPrimary = Color.White,
  primaryContainer = MedicalBlueLight,
  onPrimaryContainer = DoctorNavyPrimary,
  secondary = ClinicalTeal,
  onSecondary = Color.White,
  secondaryContainer = ClinicalTealLight,
  onSecondaryContainer = ClinicalTealDark,
  tertiary = DoctorAccentSky,
  background = NeutralBackgroundLight,
  surface = NeutralSurfaceLight,
  onBackground = NeutralTextPrimaryLight,
  onSurface = NeutralTextPrimaryLight,
  surfaceVariant = NeutralSurfaceVariantLight,
  onSurfaceVariant = NeutralTextSecondaryLight,
  outline = NeutralBorderLight,
)

val DoctorDarkColorScheme = darkColorScheme(
  primary = DoctorAccentSky,
  onPrimary = DoctorNavyPrimary,
  primaryContainer = DoctorNavySurface,
  onPrimaryContainer = DoctorAccentSky,
  secondary = DoctorTealAccent,
  onSecondary = DoctorNavyPrimary,
  background = Color(0xFF070E1A),
  surface = DoctorNavySurface,
  onBackground = DarkTextPrimary,
  onSurface = DarkTextPrimary,
  surfaceVariant = DoctorNavySecondary,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder,
)

@Composable
fun GetWellSoonTheme(
  isDoctorApp: Boolean = false,
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme: ColorScheme = if (isDoctorApp) {
    if (darkTheme) DoctorDarkColorScheme else DoctorLightColorScheme
  } else {
    if (darkTheme) PatientDarkColorScheme else PatientLightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
