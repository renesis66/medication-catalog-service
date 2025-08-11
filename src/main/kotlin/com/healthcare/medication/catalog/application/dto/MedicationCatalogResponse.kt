package com.healthcare.medication.catalog.application.dto

import com.healthcare.medication.catalog.domain.model.MedicationCatalog
import com.healthcare.medication.catalog.domain.model.MedicationCategory
import com.healthcare.medication.catalog.domain.model.MedicationStatus
import java.time.Instant

data class MedicationCatalogResponse(
    val medicationName: String,
    val category: String,
    val defaultDosageUnits: List<String>,
    val standardDosages: List<Int>,
    val contraindications: List<String>,
    val maxDailyDose: Int,
    val frequencyOptions: List<FrequencyOptionDto>,
    val status: String,
    val isActive: Boolean,
    val createdAt: Instant
)

data class FrequencyOptionDto(
    val hours: Int,
    val timesPerDay: Int,
    val description: String
)

data class MedicationCategoryResponse(
    val category: String,
    val displayName: String,
    val medications: List<MedicationCatalogResponse>
)

fun MedicationCatalog.toResponse(): MedicationCatalogResponse {
    return MedicationCatalogResponse(
        medicationName = this.medicationName.value,
        category = this.category.displayName,
        defaultDosageUnits = this.defaultDosageUnits.map { it.symbol },
        standardDosages = this.standardDosages,
        contraindications = this.contraindications,
        maxDailyDose = this.maxDailyDose,
        frequencyOptions = this.frequencyOptions.map { freq ->
            FrequencyOptionDto(
                hours = freq.hours,
                timesPerDay = freq.timesPerDay(),
                description = when (freq.hours) {
                    4 -> "Every 4 hours (6 times daily)"
                    6 -> "Every 6 hours (4 times daily)"
                    8 -> "Every 8 hours (3 times daily)"
                    12 -> "Every 12 hours (twice daily)"
                    24 -> "Once daily"
                    else -> "Every ${freq.hours} hours"
                }
            )
        },
        status = this.status.name,
        isActive = this.isActive(),
        createdAt = this.createdAt
    )
}