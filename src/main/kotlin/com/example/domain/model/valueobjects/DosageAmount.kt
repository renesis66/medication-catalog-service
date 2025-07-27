package com.example.domain.model.valueobjects

import java.math.BigDecimal

data class DosageAmount(
    val amount: BigDecimal,
    val unit: DosageUnit
) {
    init {
        require(amount > BigDecimal.ZERO) { "Dosage amount must be positive" }
        require(amount <= BigDecimal(10000)) { "Dosage amount cannot exceed 10000" }
    }
}

enum class DosageUnit(val symbol: String) {
    MILLIGRAM("mg"),
    GRAM("g"),
    MILLILITER("ml"),
    MICROGRAM("mcg"),
    INTERNATIONAL_UNIT("IU"),
    TABLET("tablet"),
    CAPSULE("capsule")
}