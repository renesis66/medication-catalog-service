package com.healthcare.medication.catalog.domain.repository

import com.healthcare.medication.catalog.domain.model.Medication
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import java.util.UUID

interface MedicationRepository {
    suspend fun findAll(): List<Medication>
    suspend fun findById(id: UUID): Medication?
    suspend fun findByName(name: MedicationName): Medication?
    suspend fun save(medication: Medication): Medication
    suspend fun delete(id: UUID): Boolean
    suspend fun existsByName(name: MedicationName): Boolean
}