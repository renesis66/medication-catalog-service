package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest
class MedicationCatalogApiControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testGetAllMedications() {
        val request = HttpRequest.GET<Any>("/medications/catalog")
        val response = client.toBlocking().exchange(request, String::class.java)
        assertEquals(200, response.status.code)
    }
    
    @Test
    fun testGetMedicationByName() {
        val request = HttpRequest.GET<Any>("/medications/catalog/amoxicillin")
        val response = client.toBlocking().exchange(request, String::class.java)
        assertEquals(200, response.status.code)
    }
    
    @Test
    fun testGetMedicationByNameNotFound() {
        val request = HttpRequest.GET<Any>("/medications/catalog/nonexistent")
        val response = client.toBlocking().exchange(request, String::class.java)
        assertEquals(404, response.status.code)
    }
    
    @Test
    fun testGetMedicationsByCategory() {
        val request = HttpRequest.GET<Any>("/medications/catalog/category/antibiotic")
        val response = client.toBlocking().exchange(request, String::class.java)
        assertEquals(200, response.status.code)
    }
    
    @Test
    fun testGetAllCategories() {
        val request = HttpRequest.GET<Any>("/medications/catalog/categories")
        val response = client.toBlocking().exchange(request, String::class.java)
        assertEquals(200, response.status.code)
    }
}