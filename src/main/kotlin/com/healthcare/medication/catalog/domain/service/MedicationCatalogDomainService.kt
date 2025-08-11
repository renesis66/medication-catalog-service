package com.healthcare.medication.catalog.domain.service

import com.healthcare.medication.catalog.domain.model.MedicationCatalog
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import com.healthcare.medication.catalog.domain.repository.MedicationCatalogRepository
import jakarta.inject.Singleton

@Singleton
class MedicationCatalogDomainService(
    private val medicationCatalogRepository: MedicationCatalogRepository
) {
    
    suspend fun validateMedicationForCreation(medication: MedicationCatalog) {
        if (medicationCatalogRepository.existsByName(medication.medicationName)) {
            throw IllegalArgumentException("Medication with name '${medication.medicationName.value}' already exists")
        }
        
        validateMedicationIntegrity(medication)
    }
    
    fun validateMedicationIntegrity(medication: MedicationCatalog) {
        if (medication.standardDosages.isEmpty()) {
            throw IllegalArgumentException("Medication must have at least one standard dosage")
        }
        
        if (medication.maxDailyDose <= 0) {
            throw IllegalArgumentException("Maximum daily dose must be positive")
        }
        
        if (medication.frequencyOptions.isEmpty()) {
            throw IllegalArgumentException("Medication must have frequency options")
        }
        
        if (medication.defaultDosageUnits.isEmpty()) {
            throw IllegalArgumentException("Medication must have default dosage units")
        }
        
        // Validate that standard dosages don't exceed max daily dose for minimum frequency
        val minFrequency = medication.frequencyOptions.minOf { it.timesPerDay() }
        val maxSingleDose = medication.standardDosages.maxOrNull() ?: 0
        val maxPossibleDaily = maxSingleDose * minFrequency
        
        if (maxPossibleDaily > medication.maxDailyDose) {
            throw IllegalArgumentException(
                "Standard dosages with minimum frequency would exceed maximum daily dose"
            )
        }
    }
    
    suspend fun canMedicationBeDeleted(medicationName: MedicationName): Boolean {
        val medication = medicationCatalogRepository.findByName(medicationName)
        return medication != null && medication.isActive()
    }
}