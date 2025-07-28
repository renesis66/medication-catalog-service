package com.healthcare.medication.catalog.domain.model

import com.healthcare.medication.catalog.domain.model.valueobjects.ActiveIngredient
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import java.time.LocalDateTime
import java.util.UUID

data class Medication(
    val id: UUID = UUID.randomUUID(),
    val name: MedicationName,
    val activeIngredients: List<ActiveIngredient>,
    val dosageGuidelines: List<DosageGuideline>,
    val drugInteractions: List<DrugInteraction>,
    val regulatoryCompliances: List<RegulatoryCompliance>,
    val description: String,
    val contraindications: List<String>,
    val sideEffects: List<String>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(activeIngredients.isNotEmpty()) { "Medication must have at least one active ingredient" }
        require(dosageGuidelines.isNotEmpty()) { "Medication must have at least one dosage guideline" }
        require(regulatoryCompliances.isNotEmpty()) { "Medication must have regulatory compliance information" }
    }
    
    fun isApproved(): Boolean = regulatoryCompliances.any { it.isCompliant() }
    
    fun hasContraindications(): Boolean = contraindications.isNotEmpty()
    
    fun getInteractionsWithSeverity(severity: InteractionSeverity): List<DrugInteraction> {
        return drugInteractions.filter { it.severity == severity }
    }
    
    fun getDosageGuideline(patientGroup: PatientGroup): DosageGuideline? {
        return dosageGuidelines.find { it.patientGroup == patientGroup }
    }
    
    fun addDrugInteraction(interaction: DrugInteraction): Medication {
        return this.copy(
            drugInteractions = drugInteractions + interaction,
            updatedAt = LocalDateTime.now()
        )
    }
    
    fun updateCompliance(compliance: RegulatoryCompliance): Medication {
        val updatedCompliances = regulatoryCompliances.map { 
            if (it.regulatoryBody == compliance.regulatoryBody) compliance else it 
        }
        return this.copy(
            regulatoryCompliances = updatedCompliances,
            updatedAt = LocalDateTime.now()
        )
    }
}