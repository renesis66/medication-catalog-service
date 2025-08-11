package com.healthcare.medication.catalog.domain.model.valueobjects

data class ActiveIngredient(
    val name: String,
    val concentration: String
) {
    init {
        require(name.isNotBlank()) { "Active ingredient name cannot be blank" }
        require(concentration.isNotBlank()) { "Concentration cannot be blank" }
        require(concentration.matches(Regex("^\\d+(\\.\\d+)?\\s?(mg|g|ml|mcg|%|IU)$"))) { 
            "Invalid concentration format" 
        }
    }
}