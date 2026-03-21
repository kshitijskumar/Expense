package org.example.project.feature.category

object CategoryValidation {
    private val VALID_NAME_REGEX = Regex("^[a-zA-Z\\s]+$")
    private const val MAX_LENGTH = 20
    
    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }
    
    fun validateCategoryName(name: String): ValidationResult {
        val trimmed = name.trim()
        
        return when {
            trimmed.isEmpty() -> 
                ValidationResult.Invalid("Category name cannot be empty")
            trimmed.length > MAX_LENGTH -> 
                ValidationResult.Invalid("Category name too long (max $MAX_LENGTH chars)")
            !trimmed.matches(VALID_NAME_REGEX) -> 
                ValidationResult.Invalid("Only letters and spaces allowed")
            else -> ValidationResult.Valid
        }
    }
}
