package com.healthcare.medication.catalog.domain.service

import com.healthcare.medication.catalog.domain.model.DrugInteraction
import com.healthcare.medication.catalog.domain.model.InteractionSeverity
import com.healthcare.medication.catalog.domain.model.Medication
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import com.healthcare.medication.catalog.domain.repository.MedicationRepository
import jakarta.inject.Singleton

@Singleton
class DrugInteractionService(
    private val medicationRepository: MedicationRepository
) {
    
    suspend fun checkInteractions(medicationNames: List<MedicationName>): List<DrugInteractionResult> {
        val medications = medicationNames.mapNotNull { name ->
            medicationRepository.findByName(name)
        }
        
        val interactions = mutableListOf<DrugInteractionResult>()
        
        for (i in medications.indices) {
            for (j in i + 1 until medications.size) {
                val med1 = medications[i]
                val med2 = medications[j]
                
                val interaction = findInteractionBetween(med1, med2)
                if (interaction != null) {
                    interactions.add(
                        DrugInteractionResult(
                            medication1 = med1.name,
                            medication2 = med2.name,
                            interaction = interaction
                        )
                    )
                }
            }
        }
        
        return interactions.sortedByDescending { it.interaction.severity.level }
    }
    
    private fun findInteractionBetween(med1: Medication, med2: Medication): DrugInteraction? {
        return med1.drugInteractions.find { interaction ->
            interaction.interactingMedication == med2.name
        } ?: med2.drugInteractions.find { interaction ->
            interaction.interactingMedication == med1.name
        }
    }
    
    fun hasContraindications(interactions: List<DrugInteractionResult>): Boolean {
        return interactions.any { it.interaction.severity == InteractionSeverity.CONTRAINDICATED }
    }
    
    fun getHighRiskInteractions(interactions: List<DrugInteractionResult>): List<DrugInteractionResult> {
        return interactions.filter { 
            it.interaction.severity in listOf(InteractionSeverity.MAJOR, InteractionSeverity.CONTRAINDICATED)
        }
    }
}

data class DrugInteractionResult(
    val medication1: MedicationName,
    val medication2: MedicationName,
    val interaction: DrugInteraction
)