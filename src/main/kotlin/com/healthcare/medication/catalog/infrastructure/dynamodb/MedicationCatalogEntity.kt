package com.healthcare.medication.catalog.infrastructure.dynamodb

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*
import java.time.Instant

@DynamoDbBean
data class MedicationCatalogEntity(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("PK")
    var pk: String = "",
    
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("SK")
    var sk: String = "",
    
    @get:DynamoDbSecondaryPartitionKey(indexNames = ["GSI1"])
    @get:DynamoDbAttribute("GSI1PK")
    var gsi1pk: String = "",
    
    @get:DynamoDbSecondarySortKey(indexNames = ["GSI1"])
    @get:DynamoDbAttribute("GSI1SK")
    var gsi1sk: String = "",
    
    @get:DynamoDbAttribute("medicationName")
    var medicationName: String = "",
    
    @get:DynamoDbAttribute("category")
    var category: String = "",
    
    @get:DynamoDbAttribute("defaultDosageUnits")
    var defaultDosageUnits: List<String> = emptyList(),
    
    @get:DynamoDbAttribute("standardDosages")
    var standardDosages: List<Int> = emptyList(),
    
    @get:DynamoDbAttribute("contraindications")
    var contraindications: List<String> = emptyList(),
    
    @get:DynamoDbAttribute("maxDailyDose")
    var maxDailyDose: Int = 0,
    
    @get:DynamoDbAttribute("frequencyOptions")
    var frequencyOptions: List<Int> = emptyList(),
    
    @get:DynamoDbAttribute("status")
    var status: String = "",
    
    @get:DynamoDbAttribute("createdAt")
    var createdAt: Instant = Instant.now()
) {
    companion object {
        fun createPK(medicationName: String): String = "MED#${medicationName.lowercase()}"
        fun createSK(): String = "METADATA"
        fun createGSI1PK(category: String): String = "CATEGORY#${category.lowercase()}"
        fun createGSI1SK(medicationName: String): String = "MED#${medicationName.lowercase()}"
    }
}