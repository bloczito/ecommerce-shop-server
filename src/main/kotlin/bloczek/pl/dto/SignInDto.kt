package bloczek.pl.dto

data class SignInDto(
    val email: String,
    val password: String,
) {
    fun validate(): String? {
        return if (email.isNullOrBlank()) "email"
        else if (password.isNullOrBlank()) "password"
        else null
    }
}
