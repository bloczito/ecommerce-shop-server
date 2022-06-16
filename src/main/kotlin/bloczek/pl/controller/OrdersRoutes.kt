package bloczek.pl.controller

import bloczek.pl.dto.NewOrderDto
import bloczek.pl.dto.OrderCreateResult
import bloczek.pl.dto.OrderDto
import bloczek.pl.dto.validate
import bloczek.pl.service.OrdersService
import bloczek.pl.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

//@Serializable
//@Resource("/orders")
//class OrdersRoutes() {
//
//}

fun Route.ordersRoutes() {

    val ordersService: OrdersService by inject(OrdersService::class.java)
    val userService: UserService by inject(UserService::class.java)

    route("/orders") {
        get {
            call.principal<JWTPrincipal>()?.getClaim("userId", Int::class)?.run {
                call.respond(ordersService.getOrders(this).map { ordersService.mapOrderToDto(it) })
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        post {
            val newOrder = call.receive<NewOrderDto>()

            if (newOrder.validate() != null) {
                call.respond(HttpStatusCode.BadRequest, "Missing required fields: ${newOrder.validate()}")
            }
            call.principal<JWTPrincipal>()?.getClaim("userId", Int::class)?.run {
                val (orderId, orderNumber) = ordersService.createOrder(newOrder, this)

                call.respond(HttpStatusCode.Created, OrderCreateResult(orderId, orderNumber))
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

}



