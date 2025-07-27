package com.example

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

@MicronautTest
class MedicationControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testGetAllMedications() {
        val request = HttpRequest.GET<Any>("/medications")
        val response = client.toBlocking().exchange(request, String::class.java)
        assertEquals(200, response.status.code)
    }
}