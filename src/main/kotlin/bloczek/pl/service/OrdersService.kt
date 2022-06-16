package bloczek.pl.service

import bloczek.pl.dto.CartItemDto
import bloczek.pl.dto.NewOrderDto
import bloczek.pl.dto.OrderDto
import bloczek.pl.dto.ProductDto
import bloczek.pl.model.Order
import bloczek.pl.repository.OrderElementsRepository
import bloczek.pl.repository.OrdersRepository
import java.time.LocalDate


class OrdersService(private val ordersRepo: OrdersRepository, private val ordersElementRepo: OrderElementsRepository) {

    suspend fun getOrders(userId: Int): List<Order> = ordersRepo.getByUserId(userId)

    suspend fun getById(orderId: Int): Order? = ordersRepo.getById(orderId)

    suspend fun createOrder(dto: NewOrderDto, userId: Int): Pair<Int, String> {
        val (name, email, city, street, postcode) = dto
        val orderId = ordersRepo.createOrder(name, email, city, street, postcode, userId, createOrderNumber())
        dto.items.forEach {
            ordersElementRepo.createOrderElement(it.productId, it.quantity, orderId)
        }

        return Pair(orderId, getById(orderId)!!.orderNumber)
    }

    private suspend fun createOrderNumber(): String {
        val now = LocalDate.now()
        return "${now.year}${now.monthValue}${ordersRepo.getOrdersCount().toString().padStart(4, '0')}"
    }


    fun mapOrderToDto(order: Order) = OrderDto(
        id = order.id,
        customerName = order.customerName,
        email = order.email,
        orderNumber = order.orderNumber,
        orderDate = order.orderDate.toString(),
        city = order.city,
        street = order.street,
        postcode = order.postcode,
        items = order.items.map { CartItemDto(
            id = it.id,
            quantity =  it.quantity,
            product = it.product.let { p ->
                ProductDto(
                    id = p.id,
                    name = p.name,
                    price = p.price,
                    url = p.url,
                    description = p.description,
                    brand = p.brand.name,
                    categoryDto = p.category,
                    subcategory = p.subcategory
                )
            }
        ) }
    )

}