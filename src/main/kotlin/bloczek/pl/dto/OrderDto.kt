package bloczek.pl.dto

data class OrderDto(
    val id: Int,
    val customerName: String,
    val email: String,
    val orderNumber: String,
    val orderDate: String,
    val city: String,
    val street: String,
    val postcode: String,
    val items: List<CartItemDto>
)
