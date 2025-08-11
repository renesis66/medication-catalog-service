package com.healthcare.medication.catalog.domain.model

import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import com.healthcare.medication.catalog.domain.model.valueobjects.DosageAmount
import com.healthcare.medication.catalog.domain.model.valueobjects.DosageUnit
import java.time.Instant

data class MedicationCatalog(
    val medicationName: MedicationName,
    val category: MedicationCategory,
    val defaultDosageUnits: List<DosageUnit>,
    val standardDosages: List<Int>,
    val contraindications: List<String>,
    val maxDailyDose: Int,
    val frequencyOptions: List<FrequencyHours>,
    val status: MedicationStatus,
    val createdAt: Instant = Instant.now()
) {
    init {
        require(standardDosages.isNotEmpty()) { "Medication must have at least one standard dosage" }
        require(maxDailyDose > 0) { "Maximum daily dose must be positive" }
        require(frequencyOptions.isNotEmpty()) { "Medication must have frequency options" }
        require(defaultDosageUnits.isNotEmpty()) { "Medication must have default dosage units" }
    }
    
    fun isActive(): Boolean = status == MedicationStatus.ACTIVE
    
    fun isValidDosage(dosage: Int): Boolean = dosage in standardDosages
    
    fun hasContraindication(condition: String): Boolean = 
        contraindications.any { it.equals(condition, ignoreCase = true) }
    
    fun getRecommendedFrequency(): FrequencyHours? = frequencyOptions.minByOrNull { it.hours }
    
    fun validateDailyDose(totalDose: Int): Boolean = totalDose <= maxDailyDose
}

enum class MedicationCategory(val displayName: String) {
    ANTIBIOTIC("Antibiotic"),
    ANALGESIC("Analgesic"), 
    ANTI_INFLAMMATORY("Anti-inflammatory"),
    ANTIHYPERTENSIVE("Antihypertensive"),
    ANTIDIABETIC("Antidiabetic"),
    ANTIHISTAMINE("Antihistamine"),
    BRONCHODILATOR("Bronchodilator"),
    DIURETIC("Diuretic"),
    SEDATIVE("Sedative"),
    VITAMIN("Vitamin"),
    OTHER("Other")
}

enum class MedicationStatus {
    ACTIVE,
    INACTIVE,
    DISCONTINUED,
    UNDER_REVIEW
}

@JvmInline
value class FrequencyHours(val hours: Int) {
    init {
        require(hours in listOf(4, 6, 8, 12, 24)) { "Frequency must be 4, 6, 8, 12, or 24 hours" }
    }
    
    fun timesPerDay(): Int = 24 / hours
}