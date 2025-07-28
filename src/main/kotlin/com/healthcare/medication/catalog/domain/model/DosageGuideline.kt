package com.healthcare.medication.catalog.domain.model

import com.healthcare.medication.catalog.domain.model.valueobjects.DosageAmount
import java.util.UUID

data class DosageGuideline(
    val id: UUID = UUID.randomUUID(),
    val patientGroup: PatientGroup,
    val recommendedDosage: DosageAmount,
    val maximumDailyDosage: DosageAmount,
    val frequency: DosageFrequency,
    val specialInstructions: String?
) {
    fun isValidDosage(proposedDosage: DosageAmount): Boolean {
        return proposedDosage.amount <= maximumDailyDosage.amount && 
               proposedDosage.unit == maximumDailyDosage.unit
    }
}

enum class PatientGroup {
    ADULT,
    PEDIATRIC,
    GERIATRIC,
    PREGNANT,
    BREASTFEEDING
}

enum class DosageFrequency(val timesPerDay: Int, val description: String) {
    ONCE_DAILY(1, "Once daily"),
    TWICE_DAILY(2, "Twice daily"),
    THREE_TIMES_DAILY(3, "Three times daily"),
    FOUR_TIMES_DAILY(4, "Four times daily"),
    AS_NEEDED(0, "As needed")
}