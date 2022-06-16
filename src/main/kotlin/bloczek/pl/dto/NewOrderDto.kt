package bloczek.pl.dto

data class NewOrderDto(
    val customerName: String,
    val email: String,

    val city: String,
    val street: String,
    val postcode: String,

    val items: List<NewCartItemDto>
)

data class NewCartItemDto(
    val productId: Int,
    val quantity: Int
)


fun NewOrderDto.validate(): String? {
    return if (customerName == null || customerName.isBlank()) "name"
    else if (email == null || email.isBlank()) "email"
    else if (city == null || city.isBlank()) "city"
    else if (street == null || street.isBlank()) "street"
    else if (postcode == null || postcode.isBlank()) "postcode"
    else if (items == null || items.size == 0) "items"
    else null
}