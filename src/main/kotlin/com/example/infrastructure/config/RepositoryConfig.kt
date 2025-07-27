package com.example.infrastructure.config

import com.example.domain.repository.MedicationCatalogRepository
import com.example.infrastructure.dynamodb.MedicationCatalogRepositoryImpl
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton

@Factory
@Requires(notEnv = ["test"])
class RepositoryConfig {
    
    @Singleton
    fun medicationCatalogRepository(impl: MedicationCatalogRepositoryImpl): MedicationCatalogRepository {
        return impl
    }
}