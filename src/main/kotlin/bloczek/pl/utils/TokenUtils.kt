package bloczek.pl.utils

import bloczek.pl.model.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateToken(
    user: User,
    secret: String,
    issuer: String,
    audience: String,
    expirationTime: Long
): String = JWT
    .create()
    .withAudience(audience)
    .withIssuer(issuer)
    .withClaim("username", user.username)
    .withClaim("userId", user.id)
    .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
    .sign(Algorithm.HMAC256(secret))