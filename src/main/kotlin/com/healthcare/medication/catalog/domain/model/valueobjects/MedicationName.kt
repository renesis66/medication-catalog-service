package com.healthcare.medication.catalog.domain.model.valueobjects

data class MedicationName(val value: String) {
    init {
        require(value.isNotBlank()) { "Medication name cannot be blank" }
        require(value.length <= 100) { "Medication name cannot exceed 100 characters" }
        require(value.matches(Regex("^[a-zA-Z0-9\\s\\-]+$"))) { "Medication name contains invalid characters" }
    }
}