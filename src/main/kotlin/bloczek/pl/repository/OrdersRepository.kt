package bloczek.pl.repository

import bloczek.pl.db.DatabaseFactory.dbQuery
import bloczek.pl.model.*
import bloczek.pl.utils.mapRowToProduct
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class OrdersRepository {

    suspend fun getById(orderId: Int): Order? = dbQuery {
        Orders.select(Orders.id eq orderId)
            .elementAtOrNull(0)
            ?.let {
                val orderElements = (OrderElements innerJoin Products innerJoin Brands)
                    .select(OrderElements.orderId eq it[Orders.id])
                    .map { it2 ->
                        OrderElement(
                            id = it2[OrderElements.id],
                            product = mapRowToProduct(it2),
                            quantity = it2[OrderElements.quantity]
                        )
                    }

                Order(
                    id = it[Orders.id],
                    customerName = it[Orders.customerName],
                    email = it[Orders.email],
                    orderNumber = it[Orders.orderNumber],
                    orderDate = it[Orders.orderDate],
                    city = it[Orders.city],
                    street = it[Orders.street],
                    postcode = it[Orders.postcode],
                    items = orderElements
                )
            }
    }

    suspend fun getByUserId(userId: Int): List<Order> = dbQuery {
        Orders
            .select(Orders.userId eq userId)
            .orderBy(Orders.orderDate, SortOrder.DESC)
            .map {
                val orderElements = (OrderElements innerJoin Products innerJoin Brands)
                    .select(OrderElements.orderId eq it[Orders.id])
                    .map { it2 ->
                        OrderElement(
                            id = it2[OrderElements.id],
                            product = mapRowToProduct(it2),
                            quantity = it2[OrderElements.quantity]
                        )
                    }

                Order(
                    id = it[Orders.id],
                    customerName = it[Orders.customerName],
                    email = it[Orders.email],
                    orderNumber = it[Orders.orderNumber],
                    orderDate = it[Orders.orderDate],
                    city = it[Orders.city],
                    street = it[Orders.street],
                    postcode = it[Orders.postcode],
                    items = orderElements
                )
            }
    }

    suspend fun createOrder(
        customerName: String,
        email: String,
        city: String,
        street: String,
        postcode: String,
        userId: Int,
        orderNumber: String
    ): Int = dbQuery {
        Orders.insert {
            it[Orders.customerName] = customerName
            it[Orders.email] = email
            it[Orders.orderNumber] = orderNumber
            it[Orders.orderDate] = LocalDateTime.now()
            it[Orders.city] = city
            it[Orders.street] = street
            it[Orders.postcode] = postcode
            it[Orders.userId] = userId
        }[Orders.id]
    }

    suspend fun getOrdersCount(): Long {
        val now = LocalDateTime.now()
        val from = now.withDayOfMonth(1)
        val to = now.withDayOfMonth(now.month.length(now.toLocalDate().isLeapYear))

        return dbQuery {
            Orders
                .select(Orders.orderDate greaterEq from)
                .andWhere { Orders.orderDate lessEq to }
                .count()
        }
    }

}