package com.example.infrastructure.config

import com.example.domain.repository.MedicationCatalogRepository
import com.example.domain.service.MedicationDomainService
import com.example.domain.service.MedicationCatalogDomainService
import com.example.infrastructure.dynamodb.MedicationCatalogRepositoryImpl
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class RepositoryConfig {
    
    @Singleton
    fun medicationCatalogRepository(impl: MedicationCatalogRepositoryImpl): MedicationCatalogRepository {
        return impl
    }
    
    @Singleton
    fun medicationDomainService(catalogService: MedicationCatalogDomainService): MedicationDomainService {
        return catalogService
    }
}