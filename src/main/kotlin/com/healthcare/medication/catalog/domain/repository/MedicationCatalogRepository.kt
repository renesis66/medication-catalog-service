package com.healthcare.medication.catalog.domain.repository

import com.healthcare.medication.catalog.domain.model.MedicationCatalog
import com.healthcare.medication.catalog.domain.model.MedicationCategory
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName

interface MedicationCatalogRepository {
    suspend fun findByName(medicationName: MedicationName): MedicationCatalog?
    suspend fun findByCategory(category: MedicationCategory): List<MedicationCatalog>
    suspend fun findAll(): List<MedicationCatalog>
    suspend fun save(medication: MedicationCatalog): MedicationCatalog
    suspend fun delete(medicationName: MedicationName): Boolean
    suspend fun existsByName(medicationName: MedicationName): Boolean
}