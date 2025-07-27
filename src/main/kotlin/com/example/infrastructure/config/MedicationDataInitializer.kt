package com.example.infrastructure.config

import com.example.domain.model.*
import com.example.domain.model.valueobjects.MedicationName
import com.example.domain.model.valueobjects.DosageUnit
import com.example.domain.repository.MedicationCatalogRepository
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Singleton
@Requires(notEnv = ["test"])
class MedicationDataInitializer(
    private val medicationCatalogRepository: MedicationCatalogRepository
) : ApplicationEventListener<StartupEvent> {
    
    override fun onApplicationEvent(event: StartupEvent) {
        runBlocking {
            initializeSampleData()
        }
    }
    
    private suspend fun initializeSampleData() {
        if (medicationCatalogRepository.findAll().isEmpty()) {
            val sampleMedications = listOf(
                createAmoxicillin(),
                createIbuprofen(),
                createAspirin(),
                createLisinopril(),
                createMetformin()
            )
            
            sampleMedications.forEach { medication ->
                medicationCatalogRepository.save(medication)
            }
        }
    }
    
    private fun createAmoxicillin(): MedicationCatalog {
        return MedicationCatalog(
            medicationName = MedicationName("amoxicillin"),
            category = MedicationCategory.ANTIBIOTIC,
            defaultDosageUnits = listOf(DosageUnit.MILLIGRAM, DosageUnit.TABLET),
            standardDosages = listOf(250, 500, 875),
            contraindications = listOf("penicillin_allergy"),
            maxDailyDose = 3000,
            frequencyOptions = listOf(FrequencyHours(8), FrequencyHours(12), FrequencyHours(24)),
            status = MedicationStatus.ACTIVE
        )
    }
    
    private fun createIbuprofen(): MedicationCatalog {
        return MedicationCatalog(
            medicationName = MedicationName("ibuprofen"),
            category = MedicationCategory.ANTI_INFLAMMATORY,
            defaultDosageUnits = listOf(DosageUnit.MILLIGRAM, DosageUnit.TABLET),
            standardDosages = listOf(200, 400, 600, 800),
            contraindications = listOf("nsaid_allergy", "peptic_ulcer", "severe_renal_impairment"),
            maxDailyDose = 2400,
            frequencyOptions = listOf(FrequencyHours(6), FrequencyHours(8), FrequencyHours(12)),
            status = MedicationStatus.ACTIVE
        )
    }
    
    private fun createAspirin(): MedicationCatalog {
        return MedicationCatalog(
            medicationName = MedicationName("aspirin"),
            category = MedicationCategory.ANALGESIC,
            defaultDosageUnits = listOf(DosageUnit.MILLIGRAM, DosageUnit.TABLET),
            standardDosages = listOf(81, 325, 500),
            contraindications = listOf("salicylate_allergy", "active_peptic_ulcer", "severe_renal_impairment"),
            maxDailyDose = 4000,
            frequencyOptions = listOf(FrequencyHours(4), FrequencyHours(6), FrequencyHours(24)),
            status = MedicationStatus.ACTIVE
        )
    }
    
    private fun createLisinopril(): MedicationCatalog {
        return MedicationCatalog(
            medicationName = MedicationName("lisinopril"),
            category = MedicationCategory.ANTIHYPERTENSIVE,
            defaultDosageUnits = listOf(DosageUnit.MILLIGRAM, DosageUnit.TABLET),
            standardDosages = listOf(5, 10, 20, 40),
            contraindications = listOf("ace_inhibitor_allergy", "pregnancy", "hyperkalemia"),
            maxDailyDose = 80,
            frequencyOptions = listOf(FrequencyHours(12), FrequencyHours(24)),
            status = MedicationStatus.ACTIVE
        )
    }
    
    private fun createMetformin(): MedicationCatalog {
        return MedicationCatalog(
            medicationName = MedicationName("metformin"),
            category = MedicationCategory.ANTIDIABETIC,
            defaultDosageUnits = listOf(DosageUnit.MILLIGRAM, DosageUnit.TABLET),
            standardDosages = listOf(500, 850, 1000),
            contraindications = listOf("severe_renal_impairment", "diabetic_ketoacidosis", "severe_heart_failure"),
            maxDailyDose = 2550,
            frequencyOptions = listOf(FrequencyHours(12), FrequencyHours(24)),
            status = MedicationStatus.ACTIVE
        )
    }
}