package com.example.domain.service

import com.example.domain.model.Medication
import com.example.domain.model.valueobjects.MedicationName
import com.example.domain.repository.MedicationRepository
import jakarta.inject.Singleton

@Singleton
class MedicationDomainService(
    private val medicationRepository: MedicationRepository
) {
    
    suspend fun validateMedicationForCreation(medication: Medication) {
        if (medicationRepository.existsByName(medication.name)) {
            throw IllegalArgumentException("Medication with name '${medication.name.value}' already exists")
        }
        
        if (!medication.isApproved()) {
            throw IllegalArgumentException("Medication must have valid regulatory approval")
        }
    }
    
    suspend fun canMedicationBeDeleted(medicationName: MedicationName): Boolean {
        val medication = medicationRepository.findByName(medicationName)
        return medication?.drugInteractions?.isEmpty() ?: true
    }
    
    fun validateMedicationIntegrity(medication: Medication) {
        if (medication.activeIngredients.isEmpty()) {
            throw IllegalArgumentException("Medication must have at least one active ingredient")
        }
        
        if (medication.dosageGuidelines.isEmpty()) {
            throw IllegalArgumentException("Medication must have dosage guidelines")
        }
        
        if (medication.regulatoryCompliances.isEmpty()) {
            throw IllegalArgumentException("Medication must have regulatory compliance data")
        }
    }
}