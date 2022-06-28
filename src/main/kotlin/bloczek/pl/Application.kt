package bloczek.pl

import bloczek.pl.controller.*
import bloczek.pl.db.DatabaseFactory
import bloczek.pl.utils.diModule
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.context.startKoin


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.mainModule() {
    startKoin {
        modules(diModule)
    }

    install(CORS) {
        allowHost("shopp-app.azurewebsites.net", schemes = listOf("http", "https")) // frontendHost might be "*"
        allowHost("localhost:3000", schemes = listOf("http", "https")) // frontendHost might be "*"
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
//        allowHeader(HttpHeaders.AccessControlAllowOrigin)
//        allowNonSimpleContentTypes = true
//        allowCredentials = true
//        allowSameOrigin = true
    }

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    val serverUrl = environment.config.property("url.server").getString()
    

    install(Authentication) {
        oauth ("auth-oauth-google") {
            urlProvider = { "$serverUrl/authenticated" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                )
            }
            client = HttpClient(CIO)
        }

        oauth ("auth-oauth-github") {
            urlProvider = { "$serverUrl/authenticated/github" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "github",
                    authorizeUrl = "https://github.com/login/oauth/authorize",
                    accessTokenUrl = "https://github.com/login/oauth/access_token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GITHUB_CLIENT_ID"),
                    clientSecret = System.getenv("GITHUB_CLIENT_SECRET")
                )
            }
            client = HttpClient(CIO)
        }

        jwt ("auth-jwt") {
            realm = myRealm
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token has expired")
            }
        }
    }

    install(ContentNegotiation) {
        gson()
        json()
    }

    install(Resources)



    DatabaseFactory.connectAndMigrate(environment.config)

    install(Routing) {
        productsRoutes()
        categoriesRoutes()
        authenticationRoutes()
        paymentRoutes()

        authenticate("auth-jwt") {
            ordersRoutes()
            usersRoute()
        }
    }



}
