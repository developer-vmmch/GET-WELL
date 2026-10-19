package com.example.model

enum class Specialty(
  val displayName: String,
  val iconName: String,
  val description: String
) {
  CARDIOLOGY("Cardiology", "Favorite", "Heart & cardiovascular care"),
  DERMATOLOGY("Dermatology", "Face", "Skin, hair & nail disorders"),
  PEDIATRICS("Pediatrics", "ChildCare", "Infant, child & teen health"),
  ORTHOPEDICS("Orthopedics", "Accessibility", "Bones, joints & spine"),
  NEUROLOGY("Neurology", "Psychology", "Brain, nerves & headache"),
  GENERAL_MEDICINE("General Medicine", "LocalHospital", "Primary healthcare & wellness"),
  DENTISTRY("Dentistry", "MedicalServices", "Oral health & teeth care"),
  OPHTHALMOLOGY("Ophthalmology", "Visibility", "Eye care & vision health")
}
