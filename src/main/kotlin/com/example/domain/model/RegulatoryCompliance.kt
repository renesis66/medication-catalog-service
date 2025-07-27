package com.example.domain.model

import com.example.domain.model.valueobjects.ComplianceStatus
import java.util.UUID

data class RegulatoryCompliance(
    val id: UUID = UUID.randomUUID(),
    val regulatoryBody: RegulatoryBody,
    val complianceStatus: ComplianceStatus,
    val regulationNumber: String,
    val complianceNotes: String?
) {
    fun isCompliant(): Boolean = complianceStatus.isValid()
}

enum class RegulatoryBody(val fullName: String, val country: String) {
    FDA("Food and Drug Administration", "United States"),
    EMA("European Medicines Agency", "European Union"),
    HC("Health Canada", "Canada"),
    TGA("Therapeutic Goods Administration", "Australia"),
    PMDA("Pharmaceuticals and Medical Devices Agency", "Japan")
}