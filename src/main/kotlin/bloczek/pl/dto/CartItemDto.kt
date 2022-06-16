package bloczek.pl.dto

data class CartItemDto(
    val id: Int,
    val quantity: Int,
    val product: ProductDto
)
