package bloczek.pl.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object OrderElements : Table() {
    val id: Column<Int> = integer("order_element_id").autoIncrement()
    val quantity: Column<Int> = integer("quantity")

    val orderId: Column<Int> = reference("order_id", Orders.id)
    val productId: Column<Int> = reference("product_id", Products.id)

    override val primaryKey = PrimaryKey(id, name = "PK_OrderElements_Id")
}


data class OrderElement(
    val id: Int,
    val product: Product,
    val quantity: Int
)