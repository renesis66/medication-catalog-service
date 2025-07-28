package com.healthcare.medication.catalog.domain.model.valueobjects

import java.time.LocalDate

data class ComplianceStatus(
    val status: RegulatoryStatus,
    val approvalDate: LocalDate?,
    val expiryDate: LocalDate?
) {
    init {
        if (status == RegulatoryStatus.APPROVED) {
            requireNotNull(approvalDate) { "Approval date required for approved medications" }
            requireNotNull(expiryDate) { "Expiry date required for approved medications" }
            require(expiryDate.isAfter(approvalDate)) { "Expiry date must be after approval date" }
        }
    }
    
    fun isExpired(): Boolean = expiryDate?.isBefore(LocalDate.now()) ?: false
    fun isValid(): Boolean = status == RegulatoryStatus.APPROVED && !isExpired()
}

enum class RegulatoryStatus {
    PENDING,
    APPROVED,
    SUSPENDED,
    RECALLED,
    WITHDRAWN
}