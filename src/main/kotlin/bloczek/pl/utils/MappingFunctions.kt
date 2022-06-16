package bloczek.pl.utils

import bloczek.pl.model.Brand
import bloczek.pl.model.Brands
import bloczek.pl.model.Product
import bloczek.pl.model.Products
import org.jetbrains.exposed.sql.ResultRow

fun mapRowToProduct(row: ResultRow) = Product(
    id = row[Products.id],
    name = row[Products.name],
    price = row[Products.price].toDouble(),
    url = row[Products.url],
    description = row[Products.description],
    brand = Brand(id = row[Brands.id], name = row[Brands.name]),
    category = row[Products.category],
    subcategory = row[Products.subcategory]
)