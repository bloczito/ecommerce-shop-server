package bloczek.pl.controller

import bloczek.pl.dto.SignInDto
import bloczek.pl.dto.SignUpDto
import bloczek.pl.enums.AccountType
import bloczek.pl.model.User
import bloczek.pl.service.UserService
import bloczek.pl.utils.generateToken
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent
import java.util.*

const val GOOGLE_API_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo"
const val GITHUB_API_INFO_URL = "https://api.github.com/user"


val asd = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson()
    }
}

fun Route.authenticationRoutes(httpClient: HttpClient = asd) {
    val userService: UserService by KoinJavaComponent.inject(UserService::class.java)
//    val frintendURL = environment.config.property("jwt.secret").toString()

    val secret = environment!!.config.property("jwt.secret").getString()
    val issuer = environment!!.config.property("jwt.issuer").getString()
    val audience = environment!!.config.property("jwt.audience").getString()
    val expirationTime = environment!!.config.property("jwt.expirationTime").getString().toLong()

    val clientUrl = environment!!.config.property("url.client").getString()

    authenticate("auth-oauth-google") {
        get("/login") {
            call.parameters.apply {  }
        }

        get("/authenticated") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()

            principal?.let {
                val userInfo: UserInfo = httpClient.get(GOOGLE_API_INFO_URL) {
                    headers {
                        append(HttpHeaders.Authorization, "${principal.tokenType} ${principal.accessToken}")
                    }
                }.body()

                val user = userService.getOrCreateUser(userInfo.id, userInfo.name, AccountType.GOOGLE)

                val token = generateToken(user, secret, issuer, audience, expirationTime)


//                call.response.cookies.append(
//                    Cookie(
//                        "auth-token",
//                        token,
//                        CookieEncoding.URI_ENCODING,
//                        expirationTime.toInt(),
//                    )
//                )

                call.respondRedirect("$clientUrl?token=$token")
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized, "ELO")
            }
        }
    }

    authenticate ("auth-oauth-github") {
        get("/login/github") {}

        get("/authenticated/github") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()

            principal?.let {
                val userInfo: GithubUserInfo = httpClient.get(GITHUB_API_INFO_URL) {
                    headers {
                        append(HttpHeaders.Authorization, "token ${principal.accessToken}")
                    }
                }.body()

                val user = userService.getOrCreateUser(userInfo.id, userInfo.name ?: userInfo.login, AccountType.GITHUB)

                val token = generateToken(user, secret, issuer, audience, expirationTime)

                call.response.headers.append(HttpHeaders.AccessControlExposeHeaders, "Set-Cookie")
//                call.response.cookies.append(
//                    Cookie(
//                        "auth-token",
//                        token,
//                        CookieEncoding.URI_ENCODING,
//                        expirationTime.toInt(),
//                        path = "/"
//                    )
//                )

                call.respondRedirect("$clientUrl?token=$token")
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized, "NAURA")
            }
        }
    }

    post("/signUp") {
        val dto = call.receive<SignUpDto>()

        dto.validate()?. run {
            call.respond(HttpStatusCode.BadRequest, "Missing required fields: $this")
        } ?: run {
            userService.createDefaultUser(dto).let { userService.getById(it) }
            call.respond(HttpStatusCode.Created)
        }
    }

    post("/signIn") {
        val dto = call.receive<SignInDto>()

        dto.validate()?.run {
            call.respond(HttpStatusCode.BadRequest, "Missing required fields: $this")
        } ?: run {
            userService.signIn(dto.email, dto.password)?.run {
                val token = generateToken(this, secret, issuer, audience, expirationTime)

                call.response.headers.append(HttpHeaders.AccessControlExposeHeaders, "Set-Cookie")
                call.response.cookies.append(
                    Cookie(
                        "auth-token",
                        token,
                        CookieEncoding.URI_ENCODING,
                        expirationTime.toInt(),
                        path = "/signUp"
                    )
                )

                call.respond(token)
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }

    get("/isEmailAvailable") {
        call.parameters["email"]?.run {
            call.respond(userService.isEmailAvailable(this))
        } ?: run {
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}



@Serializable
data class UserInfo(
    val id: String,
    val name: String,
)

@Serializable
data class GithubUserInfo(
    val id: String,
    val login: String,
    val name: String?,
)