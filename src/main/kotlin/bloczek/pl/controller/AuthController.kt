package bloczek.pl.controller

import bloczek.pl.enums.AccountType
import bloczek.pl.service.ProductService
import bloczek.pl.service.UserService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.people.v1.PeopleService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.gson.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
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


    authenticate("auth-oauth-google") {
        get("/login") {}

        get("/authenticated") {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()

            principal?.let {
                val userInfo: UserInfo = httpClient.get(GOOGLE_API_INFO_URL) {
                    headers {
                        append(HttpHeaders.Authorization, "${principal.tokenType} ${principal.accessToken}")
                    }
                }.body()

                val user = userService.getOrCreateUser(userInfo.id, userInfo.name, AccountType.GOOGLE)

                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
                    .sign(Algorithm.HMAC256(secret))


                call.response.cookies.append(
                    Cookie(
                        "auth-token",
                        token,
                        CookieEncoding.URI_ENCODING,
                        expirationTime.toInt()
                    )
                )

                call.respondRedirect("http://localhost:3000")
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

                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
                    .sign(Algorithm.HMAC256(secret))

                call.response.headers.append(HttpHeaders.AccessControlExposeHeaders, "Set-Cookie")
                call.response.cookies.append(
                    Cookie(
                        "auth-token",
                        token,
                        CookieEncoding.URI_ENCODING,
                        expirationTime.toInt(),
                        path = "/"
                    )
                )

                call.respondRedirect("http://localhost:3000")
            } ?: run {
                call.respond(HttpStatusCode.Unauthorized, "NAURA")
            }
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