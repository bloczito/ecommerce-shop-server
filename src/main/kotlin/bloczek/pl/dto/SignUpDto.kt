package bloczek.pl.dto

data class SignUpDto(
    val email: String,
    val password: String,
    val name: String? = null,
    val street: String? = null,
    val postcode: String? = null,
    val city: String? = null
) {
    fun validate(): String? {
        return if (email.isNullOrBlank()) "email"
        else if (password.isNullOrBlank()) "password"
        else null
    }
}
