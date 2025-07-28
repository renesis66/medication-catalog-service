package com.healthcare.medication.catalog.application.dto

import com.healthcare.medication.catalog.domain.model.*
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import com.healthcare.medication.catalog.domain.model.valueobjects.DosageUnit
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class CreateMedicationCatalogRequest(
    val medicationName: String,
    val category: String,
    val defaultDosageUnits: List<String>,
    val standardDosages: List<Int>,
    val contraindications: List<String> = emptyList(),
    val maxDailyDose: Int,
    val frequencyOptions: List<Int>,
    val status: String = "ACTIVE"
)

fun CreateMedicationCatalogRequest.toDomain(): MedicationCatalog {
    return MedicationCatalog(
        medicationName = MedicationName(this.medicationName),
        category = MedicationCategory.valueOf(this.category.uppercase()),
        defaultDosageUnits = this.defaultDosageUnits.map { unit ->
            DosageUnit.valueOf(unit.uppercase())
        },
        standardDosages = this.standardDosages,
        contraindications = this.contraindications,
        maxDailyDose = this.maxDailyDose,
        frequencyOptions = this.frequencyOptions.map { FrequencyHours(it) },
        status = MedicationStatus.valueOf(this.status.uppercase())
    )
}