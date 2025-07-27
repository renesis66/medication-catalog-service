package com.example.application.service

import com.example.application.dto.CreateMedicationRequest
import com.example.application.dto.MedicationResponse
import com.example.application.dto.toDomain
import com.example.application.dto.toResponse
import com.example.domain.model.valueobjects.MedicationName
import com.example.domain.repository.MedicationRepository
import com.example.domain.service.MedicationDomainService
import jakarta.inject.Singleton

@Singleton
class MedicationCatalogService(
    private val medicationRepository: MedicationRepository,
    private val medicationDomainService: MedicationDomainService
) {
    
    suspend fun getAllMedications(): List<MedicationResponse> {
        return medicationRepository.findAll()
            .filter { it.isApproved() }
            .map { it.toResponse() }
    }
    
    suspend fun getMedicationByName(medicationName: String): MedicationResponse? {
        val name = MedicationName(medicationName)
        val medication = medicationRepository.findByName(name)
        
        return if (medication?.isApproved() == true) {
            medication.toResponse()
        } else {
            null
        }
    }
    
    suspend fun createMedication(request: CreateMedicationRequest): MedicationResponse {
        val medication = request.toDomain()
        
        medicationDomainService.validateMedicationForCreation(medication)
        medicationDomainService.validateMedicationIntegrity(medication)
        
        val savedMedication = medicationRepository.save(medication)
        return savedMedication.toResponse()
    }
    
    suspend fun medicationExists(medicationName: String): Boolean {
        val name = MedicationName(medicationName)
        return medicationRepository.existsByName(name)
    }
}