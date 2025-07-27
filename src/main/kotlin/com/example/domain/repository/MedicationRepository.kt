package com.example.domain.repository

import com.example.domain.model.Medication
import com.example.domain.model.valueobjects.MedicationName
import java.util.UUID

interface MedicationRepository {
    suspend fun findAll(): List<Medication>
    suspend fun findById(id: UUID): Medication?
    suspend fun findByName(name: MedicationName): Medication?
    suspend fun save(medication: Medication): Medication
    suspend fun delete(id: UUID): Boolean
    suspend fun existsByName(name: MedicationName): Boolean
}