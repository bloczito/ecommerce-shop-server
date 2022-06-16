package bloczek.pl.controller

import bloczek.pl.dto.CreatePaymentDto
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.paymentRoutes() {

    Stripe.apiKey = System.getenv("STRIPE_API_KEY")

    post("/createPayment") {
        val dto = call.receive<CreatePaymentDto>()

        val paymentParams: PaymentIntentCreateParams = PaymentIntentCreateParams.builder()
            .setAmount((dto.amount * 100).toLong())
            .setCurrency("pln")
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods
                    .builder()
                    .setEnabled(true)
                    .build()
            )
            .build()

        val payment: PaymentIntent = PaymentIntent.create(paymentParams)

        call.respond(object { val clientSecret = payment.clientSecret })
    }

}