package com.example.infrastructure.dynamodb

import com.example.domain.model.*
import com.example.domain.model.valueobjects.MedicationName
import com.example.domain.model.valueobjects.DosageUnit

fun MedicationCatalogEntity.toDomain(): MedicationCatalog? {
    return try {
        MedicationCatalog(
            medicationName = MedicationName(this.medicationName),
            category = MedicationCategory.valueOf(this.category.uppercase()),
            defaultDosageUnits = this.defaultDosageUnits.mapNotNull { unit ->
                try {
                    DosageUnit.valueOf(unit.uppercase())
                } catch (e: IllegalArgumentException) {
                    null
                }
            },
            standardDosages = this.standardDosages,
            contraindications = this.contraindications,
            maxDailyDose = this.maxDailyDose,
            frequencyOptions = this.frequencyOptions.map { FrequencyHours(it) },
            status = MedicationStatus.valueOf(this.status.uppercase()),
            createdAt = this.createdAt
        )
    } catch (e: Exception) {
        null
    }
}

fun MedicationCatalog.toEntity(): MedicationCatalogEntity {
    val medicationNameValue = this.medicationName.value
    return MedicationCatalogEntity(
        pk = MedicationCatalogEntity.createPK(medicationNameValue),
        sk = MedicationCatalogEntity.createSK(),
        gsi1pk = MedicationCatalogEntity.createGSI1PK(this.category.name),
        gsi1sk = MedicationCatalogEntity.createGSI1SK(medicationNameValue),
        medicationName = medicationNameValue,
        category = this.category.name.lowercase(),
        defaultDosageUnits = this.defaultDosageUnits.map { it.symbol },
        standardDosages = this.standardDosages,
        contraindications = this.contraindications,
        maxDailyDose = this.maxDailyDose,
        frequencyOptions = this.frequencyOptions.map { it.hours },
        status = this.status.name,
        createdAt = this.createdAt
    )
}