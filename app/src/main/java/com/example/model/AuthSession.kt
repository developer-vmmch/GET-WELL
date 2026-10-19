package com.example.model

enum class UserRole {
  PATIENT,
  DOCTOR
}

data class AuthSession(
  val isAuthenticated: Boolean = false,
  val role: UserRole = UserRole.PATIENT,
  val userId: String = "",
  val userName: String = "",
  val token: String = "",
  val emailOrPhone: String = ""
)
