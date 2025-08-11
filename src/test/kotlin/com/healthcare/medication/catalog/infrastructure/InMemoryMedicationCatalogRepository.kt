package com.healthcare.medication.catalog.infrastructure

import com.healthcare.medication.catalog.domain.model.MedicationCatalog
import com.healthcare.medication.catalog.domain.model.MedicationCategory
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import com.healthcare.medication.catalog.domain.repository.MedicationCatalogRepository
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton
@Requires(env = ["test"])
class InMemoryMedicationCatalogRepository : MedicationCatalogRepository {
    
    private val medications = ConcurrentHashMap<String, MedicationCatalog>()
    
    override suspend fun findByName(medicationName: MedicationName): MedicationCatalog? {
        return medications[medicationName.value.lowercase()]
    }
    
    override suspend fun findByCategory(category: MedicationCategory): List<MedicationCatalog> {
        return medications.values.filter { it.category == category }
    }
    
    override suspend fun findAll(): List<MedicationCatalog> {
        return medications.values.toList()
    }
    
    override suspend fun save(medication: MedicationCatalog): MedicationCatalog {
        medications[medication.medicationName.value.lowercase()] = medication
        return medication
    }
    
    override suspend fun delete(medicationName: MedicationName): Boolean {
        return medications.remove(medicationName.value.lowercase()) != null
    }
    
    override suspend fun existsByName(medicationName: MedicationName): Boolean {
        return medications.containsKey(medicationName.value.lowercase())
    }
    
    fun clear() {
        medications.clear()
    }
}