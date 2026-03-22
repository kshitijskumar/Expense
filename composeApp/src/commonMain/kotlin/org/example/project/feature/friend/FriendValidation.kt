package org.example.project.feature.friend

object FriendValidation {
    private val VALID_NAME_REGEX = Regex("^[a-zA-Z\\s'-]+$")
    private const val MAX_LENGTH = 30
    
    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val message: String) : ValidationResult()
    }
    
    fun validateFriendName(name: String): ValidationResult {
        val trimmed = name.trim()
        
        return when {
            trimmed.isEmpty() -> 
                ValidationResult.Invalid("Friend name cannot be empty")
            trimmed.length > MAX_LENGTH -> 
                ValidationResult.Invalid("Friend name too long (max $MAX_LENGTH chars)")
            !trimmed.matches(VALID_NAME_REGEX) -> 
                ValidationResult.Invalid("Only letters, spaces, hyphens and apostrophes allowed")
            else -> ValidationResult.Valid
        }
    }
}
