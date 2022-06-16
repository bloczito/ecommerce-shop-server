package bloczek.pl.repository

import bloczek.pl.db.DatabaseFactory.dbQuery
import bloczek.pl.model.OrderElement
import bloczek.pl.model.OrderElements
import org.jetbrains.exposed.sql.insert

class OrderElementsRepository {

//    suspend fun getAllByOrderId(orderId: Int): OrderElement = dbQuery {
//
//    }


    suspend fun createOrderElement(productId: Int, quantity: Int, orderId: Int): Int = dbQuery {
        OrderElements.insert {
            it[OrderElements.productId] = productId
            it[OrderElements.quantity] = quantity
            it[OrderElements.orderId] = orderId
        }[OrderElements.id]
    }
}