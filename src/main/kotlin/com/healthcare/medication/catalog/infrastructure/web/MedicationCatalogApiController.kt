package com.healthcare.medication.catalog.infrastructure.web

import com.healthcare.medication.catalog.application.dto.CreateMedicationCatalogRequest
import com.healthcare.medication.catalog.application.dto.MedicationCatalogResponse
import com.healthcare.medication.catalog.application.dto.MedicationCategoryResponse
import com.healthcare.medication.catalog.application.service.MedicationCatalogApplicationService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/medications/catalog")
class MedicationCatalogApiController(
    private val medicationCatalogService: MedicationCatalogApplicationService
) {
    
    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    suspend fun getAllMedications(): HttpResponse<List<MedicationCatalogResponse>> {
        return try {
            val medications = medicationCatalogService.getAllMedications()
            HttpResponse.ok(medications)
        } catch (e: Exception) {
            HttpResponse.serverError()
        }
    }
    
    @Get("/{medicationName}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    suspend fun getMedicationByName(@PathVariable medicationName: String): HttpResponse<MedicationCatalogResponse> {
        return try {
            val medication = medicationCatalogService.getMedicationByName(medicationName)
            if (medication != null) {
                HttpResponse.ok(medication)
            } else {
                HttpResponse.notFound()
            }
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest()
        } catch (e: Exception) {
            HttpResponse.serverError()
        }
    }
    
    @Get("/category/{categoryName}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    suspend fun getMedicationsByCategory(@PathVariable categoryName: String): HttpResponse<MedicationCategoryResponse> {
        return try {
            val categoryData = medicationCatalogService.getMedicationsByCategory(categoryName)
            if (categoryData != null) {
                HttpResponse.ok(categoryData)
            } else {
                HttpResponse.notFound()
            }
        } catch (e: Exception) {
            HttpResponse.serverError()
        }
    }
    
    @Get("/categories")
    @Secured(SecurityRule.IS_ANONYMOUS)
    suspend fun getAllCategories(): HttpResponse<List<String>> {
        return try {
            val categories = medicationCatalogService.getAllCategories()
            HttpResponse.ok(categories)
        } catch (e: Exception) {
            HttpResponse.serverError()
        }
    }
    
    @Post
    @Secured("ADMIN")
    suspend fun createMedication(@Body request: CreateMedicationCatalogRequest): HttpResponse<MedicationCatalogResponse> {
        return try {
            val medication = medicationCatalogService.createMedication(request)
            HttpResponse.status<MedicationCatalogResponse>(HttpStatus.CREATED).body(medication)
        } catch (e: IllegalArgumentException) {
            HttpResponse.badRequest()
        } catch (e: Exception) {
            HttpResponse.serverError()
        }
    }
}