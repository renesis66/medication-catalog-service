package com.healthcare.medication.catalog.domain.model

import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import java.util.UUID

data class DrugInteraction(
    val id: UUID = UUID.randomUUID(),
    val interactingMedication: MedicationName,
    val severity: InteractionSeverity,
    val description: String,
    val clinicalSignificance: String
) {
    fun isContraindicated(): Boolean = severity == InteractionSeverity.CONTRAINDICATED
    fun requiresMonitoring(): Boolean = severity in listOf(InteractionSeverity.MAJOR, InteractionSeverity.MODERATE)
}

enum class InteractionSeverity(val level: Int, val description: String) {
    MINOR(1, "Minor interaction - minimal clinical significance"),
    MODERATE(2, "Moderate interaction - may require dosage adjustment or monitoring"),
    MAJOR(3, "Major interaction - requires close monitoring or alternative therapy"),
    CONTRAINDICATED(4, "Contraindicated - should not be used together")
}