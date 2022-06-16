package bloczek.pl.controller

import bloczek.pl.dto.UserDto
import bloczek.pl.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject


fun Route.usersRoute() {

    val userService: UserService by inject(UserService::class.java)

    route("/user") {
        get {
            val principal = call.principal<JWTPrincipal>()

            principal?.getClaim("userId", Int::class)?.run {
                val user = userService.getById(this).let {
                    UserDto(it.name, it.city, it.street, it.postcode)
                }
                call.respond(user)
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post {
            val principal = call.principal<JWTPrincipal>()
            val body = call.receive<UserDto>()

            principal?.getClaim("userId", Int::class)?.run {
                userService.updateUser(this, body)
                call.respond(HttpStatusCode.Created)
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}