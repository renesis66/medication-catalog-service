package com.healthcare.medication.catalog.infrastructure.dynamodb

import com.healthcare.medication.catalog.domain.model.MedicationCatalog
import com.healthcare.medication.catalog.domain.model.MedicationCategory
import com.healthcare.medication.catalog.domain.model.valueobjects.MedicationName
import com.healthcare.medication.catalog.domain.repository.MedicationCatalogRepository
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional

@Singleton
@Requires(notEnv = ["test"])
class MedicationCatalogRepositoryImpl(
    private val enhancedClient: DynamoDbEnhancedClient
) : MedicationCatalogRepository {
    
    private val table: DynamoDbTable<MedicationCatalogEntity> = enhancedClient.table(
        "medication-catalog", 
        software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean(MedicationCatalogEntity::class.java)
    )
    
    private val gsi1: DynamoDbIndex<MedicationCatalogEntity> = table.index("GSI1")
    
    override suspend fun findByName(medicationName: MedicationName): MedicationCatalog? {
        val key = Key.builder()
            .partitionValue(MedicationCatalogEntity.createPK(medicationName.value))
            .sortValue(MedicationCatalogEntity.createSK())
            .build()
            
        val entity = table.getItem(key)
        return entity?.toDomain()
    }
    
    override suspend fun findByCategory(category: MedicationCategory): List<MedicationCatalog> {
        val queryConditional = QueryConditional.keyEqualTo(
            Key.builder()
                .partitionValue(MedicationCatalogEntity.createGSI1PK(category.name))
                .build()
        )
        
        val results = gsi1.query(queryConditional)
        return results.stream().map { page -> 
            page.items().map { it.toDomain() }
        }.flatMap { it.stream() }.filter { it != null }.map { it!! }.toList()
    }
    
    override suspend fun findAll(): List<MedicationCatalog> {
        val results = table.scan()
        return results.stream().map { page ->
            page.items().map { it.toDomain() }
        }.flatMap { it.stream() }.filter { it != null }.map { it!! }.toList()
    }
    
    override suspend fun save(medication: MedicationCatalog): MedicationCatalog {
        val entity = medication.toEntity()
        table.putItem(entity)
        return medication
    }
    
    override suspend fun delete(medicationName: MedicationName): Boolean {
        val key = Key.builder()
            .partitionValue(MedicationCatalogEntity.createPK(medicationName.value))
            .sortValue(MedicationCatalogEntity.createSK())
            .build()
            
        return try {
            table.deleteItem(key)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun existsByName(medicationName: MedicationName): Boolean {
        return findByName(medicationName) != null
    }
}