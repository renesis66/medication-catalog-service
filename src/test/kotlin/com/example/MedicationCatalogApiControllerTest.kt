package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest(environments = ["test"])
class MedicationCatalogApiControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testGetAllMedications() {
        val response = client.toBlocking().exchange(
            HttpRequest.GET<Any>("/medications/catalog"), 
            String::class.java
        )
        assertEquals(200, response.status.code)
    }
}