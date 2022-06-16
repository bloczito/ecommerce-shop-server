package bloczek.pl.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object Orders : Table() {
    val id: Column<Int> = integer("order_id").autoIncrement()

    val userId: Column<Int> = reference("user_id", Users.id)

    val customerName: Column<String> = varchar("customer_name", 255)
    val email: Column<String> = varchar("email", 255)

    val orderNumber: Column<String> = varchar("order_number", 255)
    val orderDate: Column<LocalDateTime> = datetime("order_date")

    val city: Column<String> = varchar("city", 255)
    val street: Column<String> = varchar("street", 255)
    val postcode: Column<String> = varchar("postcode", 255)

    override val primaryKey = PrimaryKey(id, name = "PK_Orders_Id")
}

data class Order(
    val id: Int,

    val customerName: String,
    val email: String,

    val orderNumber: String,
    val orderDate: LocalDateTime,

    val city: String,
    val street: String,
    val postcode: String,

    val items: List<OrderElement>
)