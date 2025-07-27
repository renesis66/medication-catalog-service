package com.example.domain.repository

import com.example.domain.model.MedicationCatalog
import com.example.domain.model.MedicationCategory
import com.example.domain.model.valueobjects.MedicationName

interface MedicationCatalogRepository {
    suspend fun findByName(medicationName: MedicationName): MedicationCatalog?
    suspend fun findByCategory(category: MedicationCategory): List<MedicationCatalog>
    suspend fun findAll(): List<MedicationCatalog>
    suspend fun save(medication: MedicationCatalog): MedicationCatalog
    suspend fun delete(medicationName: MedicationName): Boolean
    suspend fun existsByName(medicationName: MedicationName): Boolean
}