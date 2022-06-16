package bloczek.pl.dto

import bloczek.pl.enums.Category
import bloczek.pl.enums.Subcategory

data class ProductDto(
     val id: Int,
     val name: String,
     val price: Double,
     val url: String,
     val description: String? = null,
     val brand: String,
     val categoryDto: Category,
     val subcategory: Subcategory
 )
