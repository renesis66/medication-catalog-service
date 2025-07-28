package com.healthcare.medication.catalog.infrastructure.config

import com.healthcare.medication.catalog.domain.repository.MedicationCatalogRepository
import com.healthcare.medication.catalog.infrastructure.dynamodb.MedicationCatalogRepositoryImpl
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