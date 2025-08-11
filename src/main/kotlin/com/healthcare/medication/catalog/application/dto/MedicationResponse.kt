package com.healthcare.medication.catalog.application.dto

import com.healthcare.medication.catalog.domain.model.*
import com.healthcare.medication.catalog.domain.model.valueobjects.ActiveIngredient
import java.time.LocalDateTime
import java.util.UUID

data class MedicationResponse(
    val id: UUID,
    val name: String,
    val activeIngredients: List<ActiveIngredientDto>,
    val dosageGuidelines: List<DosageGuidelineDto>,
    val drugInteractions: List<DrugInteractionDto>,
    val regulatoryCompliances: List<RegulatoryComplianceDto>,
    val description: String,
    val contraindications: List<String>,
    val sideEffects: List<String>,
    val isApproved: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class ActiveIngredientDto(
    val name: String,
    val concentration: String
)

data class DosageGuidelineDto(
    val id: UUID,
    val patientGroup: String,
    val recommendedDosage: String,
    val maximumDailyDosage: String,
    val frequency: String,
    val specialInstructions: String?
)

data class DrugInteractionDto(
    val id: UUID,
    val interactingMedication: String,
    val severity: String,
    val description: String,
    val clinicalSignificance: String
)

data class RegulatoryComplianceDto(
    val id: UUID,
    val regulatoryBody: String,
    val status: String,
    val regulationNumber: String,
    val complianceNotes: String?
)

fun Medication.toResponse(): MedicationResponse {
    return MedicationResponse(
        id = this.id,
        name = this.name.value,
        activeIngredients = this.activeIngredients.map { it.toDto() },
        dosageGuidelines = this.dosageGuidelines.map { it.toDto() },
        drugInteractions = this.drugInteractions.map { it.toDto() },
        regulatoryCompliances = this.regulatoryCompliances.map { it.toDto() },
        description = this.description,
        contraindications = this.contraindications,
        sideEffects = this.sideEffects,
        isApproved = this.isApproved(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

private fun ActiveIngredient.toDto() = ActiveIngredientDto(
    name = this.name,
    concentration = this.concentration
)

private fun DosageGuideline.toDto() = DosageGuidelineDto(
    id = this.id,
    patientGroup = this.patientGroup.name,
    recommendedDosage = "${this.recommendedDosage.amount} ${this.recommendedDosage.unit.symbol}",
    maximumDailyDosage = "${this.maximumDailyDosage.amount} ${this.maximumDailyDosage.unit.symbol}",
    frequency = this.frequency.description,
    specialInstructions = this.specialInstructions
)

private fun DrugInteraction.toDto() = DrugInteractionDto(
    id = this.id,
    interactingMedication = this.interactingMedication.value,
    severity = this.severity.name,
    description = this.description,
    clinicalSignificance = this.clinicalSignificance
)

private fun RegulatoryCompliance.toDto() = RegulatoryComplianceDto(
    id = this.id,
    regulatoryBody = this.regulatoryBody.fullName,
    status = this.complianceStatus.status.name,
    regulationNumber = this.regulationNumber,
    complianceNotes = this.complianceNotes
)