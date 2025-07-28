package com.healthcare.medication.catalog.application.service

import com.healthcare.medication.catalog.application.dto.*
import com.healthcare.medication.catalog.domain.model.MedicationCategory
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import com.healthcare.medication.catalog.domain.repository.MedicationCatalogRepository
import com.healthcare.medication.catalog.domain.service.MedicationCatalogDomainService
import jakarta.inject.Singleton

@Singleton
class MedicationCatalogApplicationService(
    private val medicationCatalogRepository: MedicationCatalogRepository,
    private val medicationCatalogDomainService: MedicationCatalogDomainService
) {
    
    suspend fun getAllMedications(): List<MedicationCatalogResponse> {
        return medicationCatalogRepository.findAll()
            .filter { it.isActive() }
            .map { it.toResponse() }
    }
    
    suspend fun getMedicationByName(medicationName: String): MedicationCatalogResponse? {
        val name = MedicationName(medicationName)
        val medication = medicationCatalogRepository.findByName(name)
        
        return if (medication?.isActive() == true) {
            medication.toResponse()
        } else {
            null
        }
    }
    
    suspend fun getMedicationsByCategory(categoryName: String): MedicationCategoryResponse? {
        return try {
            val category = MedicationCategory.valueOf(categoryName.uppercase())
            val medications = medicationCatalogRepository.findByCategory(category)
                .filter { it.isActive() }
                .map { it.toResponse() }
            
            MedicationCategoryResponse(
                category = category.name,
                displayName = category.displayName,
                medications = medications
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
    
    suspend fun createMedication(request: CreateMedicationCatalogRequest): MedicationCatalogResponse {
        val medication = request.toDomain()
        
        medicationCatalogDomainService.validateMedicationForCreation(medication)
        
        val savedMedication = medicationCatalogRepository.save(medication)
        return savedMedication.toResponse()
    }
    
    suspend fun medicationExists(medicationName: String): Boolean {
        val name = MedicationName(medicationName)
        return medicationCatalogRepository.existsByName(name)
    }
    
    suspend fun getAllCategories(): List<String> {
        return MedicationCategory.values().map { it.displayName }
    }
}