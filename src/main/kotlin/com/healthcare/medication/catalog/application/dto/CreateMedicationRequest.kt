package com.healthcare.medication.catalog.application.dto

import com.healthcare.medication.catalog.domain.model.*
import com.healthcare.medication.catalog.domain.model.valueobjects.*
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import java.time.LocalDate

@Serdeable
data class CreateMedicationRequest(
    val name: String,
    val activeIngredients: List<CreateActiveIngredientRequest>,
    val dosageGuidelines: List<CreateDosageGuidelineRequest>,
    val drugInteractions: List<CreateDrugInteractionRequest> = emptyList(),
    val regulatoryCompliances: List<CreateRegulatoryComplianceRequest>,
    val description: String,
    val contraindications: List<String> = emptyList(),
    val sideEffects: List<String> = emptyList()
)

@Serdeable
data class CreateActiveIngredientRequest(
    val name: String,
    val concentration: String
)

@Serdeable
data class CreateDosageGuidelineRequest(
    val patientGroup: String,
    val recommendedDosage: CreateDosageAmountRequest,
    val maximumDailyDosage: CreateDosageAmountRequest,
    val frequency: String,
    val specialInstructions: String?
)

@Serdeable
data class CreateDosageAmountRequest(
    val amount: BigDecimal,
    val unit: String
)

@Serdeable
data class CreateDrugInteractionRequest(
    val interactingMedication: String,
    val severity: String,
    val description: String,
    val clinicalSignificance: String
)

@Serdeable
data class CreateRegulatoryComplianceRequest(
    val regulatoryBody: String,
    val status: String,
    val approvalDate: LocalDate?,
    val expiryDate: LocalDate?,
    val regulationNumber: String,
    val complianceNotes: String?
)

fun CreateMedicationRequest.toDomain(): Medication {
    return Medication(
        name = MedicationName(this.name),
        activeIngredients = this.activeIngredients.map { it.toDomain() },
        dosageGuidelines = this.dosageGuidelines.map { it.toDomain() },
        drugInteractions = this.drugInteractions.map { it.toDomain() },
        regulatoryCompliances = this.regulatoryCompliances.map { it.toDomain() },
        description = this.description,
        contraindications = this.contraindications,
        sideEffects = this.sideEffects
    )
}

private fun CreateActiveIngredientRequest.toDomain() = ActiveIngredient(
    name = this.name,
    concentration = this.concentration
)

private fun CreateDosageGuidelineRequest.toDomain() = DosageGuideline(
    patientGroup = PatientGroup.valueOf(this.patientGroup.uppercase()),
    recommendedDosage = this.recommendedDosage.toDomain(),
    maximumDailyDosage = this.maximumDailyDosage.toDomain(),
    frequency = DosageFrequency.valueOf(this.frequency.uppercase().replace(" ", "_")),
    specialInstructions = this.specialInstructions
)

private fun CreateDosageAmountRequest.toDomain() = DosageAmount(
    amount = this.amount,
    unit = DosageUnit.valueOf(this.unit.uppercase())
)

private fun CreateDrugInteractionRequest.toDomain() = DrugInteraction(
    interactingMedication = MedicationName(this.interactingMedication),
    severity = InteractionSeverity.valueOf(this.severity.uppercase()),
    description = this.description,
    clinicalSignificance = this.clinicalSignificance
)

private fun CreateRegulatoryComplianceRequest.toDomain() = RegulatoryCompliance(
    regulatoryBody = RegulatoryBody.valueOf(this.regulatoryBody.uppercase()),
    complianceStatus = ComplianceStatus(
        status = RegulatoryStatus.valueOf(this.status.uppercase()),
        approvalDate = this.approvalDate,
        expiryDate = this.expiryDate
    ),
    regulationNumber = this.regulationNumber,
    complianceNotes = this.complianceNotes
)